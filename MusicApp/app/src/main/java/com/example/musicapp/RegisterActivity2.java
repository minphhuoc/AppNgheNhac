package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.model.InfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity2 extends AppCompatActivity {

    Button btnRegister;
    SharedPreferences sharedPreferences;
    TextView txtLogin;
    private FirebaseAuth mAuth;
    EditText txtLoginedit, txtPassedit,txtTen;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        onCreate();
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });



    }

    private void register() {
        String email,pass,name;
        email=txtLoginedit.getText().toString();
        pass=txtPassedit.getText().toString();
        name=String.valueOf(txtTen.getText());



        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Vui lòng nhập Tên!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập Email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui lòng nhập Pass!!",Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {



            @Override


            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Tạo một đối tượng InfoModel mới với thông tin người dùng
                    InfoModel infoModel = new InfoModel(name);
                    // Lấy ID của người dùng mới
                    String userId = mAuth.getCurrentUser().getUid();
                     sharedPreferences= getApplicationContext().getSharedPreferences(userId, Context.MODE_PRIVATE);
                    db.collection("users")
                            .document(userId)
                            .set(infoModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity2.this, "Thông tin người dùng đã được lưu thành công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity2.this, "Lỗi khi lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Toast.makeText(getApplicationContext(),"Register Successful",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity2.this,LoginActivity.class);
                    startActivity(intent);


                }
                else {
                    Toast.makeText(getApplicationContext(),"Register Failed",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void onCreate() {
        txtTen=findViewById(R.id.txtName);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.linkLogin);
        txtLoginedit=findViewById(R.id.txtEmail);
        txtPassedit=findViewById(R.id.txtPass);
    }

}