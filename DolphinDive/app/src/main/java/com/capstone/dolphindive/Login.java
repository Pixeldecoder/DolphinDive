package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText logEmail, logPassword;
    Button logBtn;
    Button cancelBtn;
    TextView navReg;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.logEmail);
        logPassword = findViewById(R.id.logPassword);

        logBtn = findViewById(R.id.loginBtn);

        navReg = findViewById(R.id.navRegister);

        progressBar = findViewById(R.id.progressBar2);

        cancelBtn = findViewById(R.id.login_cancelBtn);

        fAuth = FirebaseAuth.getInstance();


        navReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
                Intent logintent = new Intent(Login.this, Register.class);
                startActivity(logintent);
            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       logBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               String email = logEmail.getText().toString().trim();
               String password = logPassword.getText().toString().trim();

               progressBar.setVisibility(View.VISIBLE);

               if (TextUtils.isEmpty(email)){
                   logEmail.setError("Email is required");
                   progressBar.setVisibility(View.INVISIBLE);
                   return;
               }

               if (TextUtils.isEmpty(password)){
                   logPassword.setError("Password is required");
                   progressBar.setVisibility(View.INVISIBLE);
                   return;
               }


               fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(getApplicationContext(), "User Successfully Logged In!",
                                   Toast.LENGTH_LONG).show();
                           Intent regintent = new Intent(Login.this, MainActivity.class);
                           startActivity(regintent);
                       }else{
                           progressBar.setVisibility((View.INVISIBLE));
                           Toast.makeText(getApplicationContext(), "Error!" + task.getException().getMessage(),
                                   Toast.LENGTH_LONG).show();
                       }
                   }
                });


        }
        });
    }
}