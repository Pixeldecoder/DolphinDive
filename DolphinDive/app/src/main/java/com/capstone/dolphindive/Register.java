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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText regUserName, regEmail, regPassword, regConfirm;
    Button regBtn;
    TextView navLog;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (password.length() < 6) {
                    regPassword.setError("Password need to be at least six characters");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    regConfirm.setError("Confirmed password is required");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }


                if (password.contentEquals(confirmPassword)) {
                } else {
//                    Toast.makeText(getApplicationContext(), "Password does not match",
//                            Toast.LENGTH_LONG).show();
                    regConfirm.setError("Confirmed password does not match!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                //register process
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Adding new user detail to the Realtime Database for chat purpose
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            String email = firebaseUser.getEmail();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", regEmail.getText().toString());
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", regUserName.getText().toString().toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        //Adding new user details to the FireStore for Profile Management
                                        documentReference = db.collection("Users").document(userid);
                                        Map<String, String> profile = new HashMap<>();
                                        profile.put("email", email);
                                        profile.put("numFollower", "0");
                                        profile.put("numFollowing", "0");
                                        profile.put("numPosts", "0");
                                        profile.put("name",regUserName.getText().toString());
                                        documentReference.set(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "User Successfully Created!",
                                                        Toast.LENGTH_LONG).show();

                                                Intent intent = new Intent(Register.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Error in Registration",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            });
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