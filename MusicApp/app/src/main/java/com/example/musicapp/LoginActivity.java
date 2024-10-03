package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView txtRegister,txtForget;
    private FirebaseAuth mAuth;
    EditText txtLoginedit, txtPassedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        onCreate();
        mAuth =FirebaseAuth.getInstance();
        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,FogetPassActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Login();
            }


        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }


        });

    }

    private void onCreate() {
        btnLogin= findViewById(R.id.btnLogin);
        txtRegister= findViewById(R.id.linkRegister);
        txtLoginedit=findViewById(R.id.txtloginEmail);
        txtPassedit=findViewById(R.id.txtloginPass);


        txtForget=findViewById(R.id.forgetPass);

    }
    private void Register() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity2.class);
        startActivity(intent);
    }
    private void Login() {
       String email,pass,name;
       email=txtLoginedit.getText().toString();
       pass=txtPassedit.getText().toString();


       if(TextUtils.isEmpty(email)){
           Toast.makeText(this,"Vui lòng nhập Email!!",Toast.LENGTH_SHORT).show();
           return;
       }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui lòng nhập Pass!!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);


                }
                else{
                    Toast.makeText(getApplicationContext(),"Login Failed , Check Email or Pass again !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}