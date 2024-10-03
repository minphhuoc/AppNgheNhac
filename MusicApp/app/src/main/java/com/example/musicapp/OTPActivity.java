package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.TestLooperManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OTPActivity extends AppCompatActivity {
    EditText OTP1,OTP2,OTP3,OTP4;
    Button btnVerify,btnResend;
    private boolean resendEnable=false;
    private int     resendTime=60;
    private int selectedPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OTP1=findViewById(R.id.OTP1);
        OTP2=findViewById(R.id.OTP2);
        OTP3=findViewById(R.id.OTP3);
        OTP4=findViewById(R.id.OTP4);
        btnVerify=findViewById(R.id.btnVerify);
        btnResend=findViewById(R.id.btnResend);
        final TextView otpemail= findViewById(R.id.OTPemail);
        final TextView otpphone= findViewById(R.id.OTPphone);
        final String getEmail=getIntent().getStringExtra("email");
        final String getPhone=getIntent().getStringExtra("phone");
        otpemail.setText(getEmail);
        otpphone.setText(getPhone);
        OTP1.addTextChangedListener(textMatches);
        OTP2.addTextChangedListener(textMatches);
        OTP3.addTextChangedListener(textMatches);
        OTP4.addTextChangedListener(textMatches);
        showKQ(OTP1);
        startCountDownTimer();
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnable){
                    startCountDownTimer();
                }
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String generatoOTP=OTP1.getText().toString()+OTP2.getText().toString()+OTP3.getText().toString()+OTP4.getText().toString();
                if(generatoOTP.length()==4){

                }
            }
        });



    }
    private void showKQ(EditText OTP){
        OTP.requestFocus();
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(OTP,InputMethodManager.SHOW_IMPLICIT);

    }
    private void startCountDownTimer(){
        resendEnable = false;
        btnResend.setTextColor(Color.parseColor("#99000000"));
        new CountDownTimer(resendTime*1000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                btnResend.setText("Resend Code ("+millisUntilFinished/1000+")");
            }

            @Override
            public void onFinish() {
                resendEnable= true;
                btnResend.setText("Resend Code");
                btnResend.setTextColor(getResources().getColor(R.color.black1));

            }
        }.start();

    }
    private final TextWatcher textMatches= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>0){
                if(selectedPosition==0){
                    selectedPosition=1;
                    showKQ(OTP2);

                }else if(selectedPosition==1){
                    selectedPosition=2;
                    showKQ(OTP3);
                }else if(selectedPosition==2){
                    selectedPosition=3;
                    showKQ(OTP4);
                }
            }

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DEL){
            if(selectedPosition==3){
                selectedPosition=2;
                showKQ(OTP3);
            }else if(selectedPosition==2){
                selectedPosition=1;
                showKQ(OTP2);
            }else if(selectedPosition==1){
                selectedPosition=0;
                showKQ(OTP1);
            }
            return true;
        }else{
            return super.onKeyUp(keyCode, event);

        }

    }
}