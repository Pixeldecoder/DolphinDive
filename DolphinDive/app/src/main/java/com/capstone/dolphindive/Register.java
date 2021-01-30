package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText regUserName, regEmail, regPassword, regConfirm;
    Button regBtn;
    TextView navLog;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUserName = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regConfirm = findViewById(R.id.regConfirm);

        regBtn = findViewById(R.id.regBtn);

        navLog = findViewById(R.id.navLog);

        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            Intent regintent = new Intent(Register.this, MainActivity.class);
            startActivity(regintent);
            finish();
        }

        navLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
                Intent registerintent = new Intent(Register.this, Login.class);
                startActivity(registerintent);
            }

        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = regUserName.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim();
                String confirmPassword = regConfirm.getText().toString().trim();


                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(userName)) {
                    regUserName.setError("UserName is required");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (password.length() < 6) {
                    regPassword.setError("Password need to be at least six characters");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    regConfirm.setError("Confirmed password is required");
                    progressBar.setVisibility(View.INVISIBLE);
                }

                if (password.contentEquals(confirmPassword)) {
                } else {
                    Toast.makeText(getApplicationContext(), "Password does not match!",
                            Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

                //register process
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Successfully Created!",
                                    Toast.LENGTH_LONG).show();
                            Intent regintent = new Intent(Register.this, Login.class);
                            startActivity(regintent);
                        } else {
                            progressBar.setVisibility((View.INVISIBLE));
                            Toast.makeText(getApplicationContext(), "Error!" + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
});
    }}