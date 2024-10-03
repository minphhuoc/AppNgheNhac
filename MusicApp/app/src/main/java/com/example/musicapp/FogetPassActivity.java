package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Toast;

import com.example.musicapp.databinding.ActivityFogetPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FogetPassActivity extends AppCompatActivity {
    ActivityFogetPassBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFogetPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(FogetPassActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");
        binding.btnGuiFG.setOnClickListener(view -> {
            forgotPass();
        });

    }

    private void forgotPass() {
        if(!Xuly()) return;
        dialog.show();
        auth.sendPasswordResetEmail(binding.txtloginEmailFG.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if(task.isSuccessful()){
                    startActivity(new Intent(FogetPassActivity.this,LoginActivity.class));
                    finish();
                    Toast.makeText(FogetPassActivity.this,"Check your mail",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(FogetPassActivity.this,"Enter Correct Email",Toast.LENGTH_SHORT).show();                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FogetPassActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    private Boolean Xuly(){
        String val = binding.txtloginEmailFG.getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty()){
            binding.txtloginEmailFG.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(emailPattern))
        {
            binding.txtloginEmailFG.setError("Invalid email address");
            return false;
        }else {
            binding.txtloginEmailFG.setError(null);
            return true;
        }
    }
}