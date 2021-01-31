package com.capstone.dolphindive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.capstone.dolphindive.utility.data.model.LoggedInUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileManagement extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseUser firebaseUser;
    private EditText email;
    private EditText name;
    private EditText phone;
    private EditText password;
    private ImageView userImage;
    private Button btnUpdateImage;
    private Button btnUpdateName;
    private Button btnUpdateEmail;
    private Button btnUpdatePhone;
    private Button btnUpdatePassword;
    private static final int PICKFILE_RESULT_CODE = 2;
    private String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email =  (EditText) findViewById(R.id.etEmail);
        name =  (EditText) findViewById(R.id.etName);
        phone =  (EditText) findViewById(R.id.etPhone);
        userImage = (ImageView)findViewById(R.id.ivUserImage);
        password = (EditText)findViewById(R.id.etPassword);
        btnUpdateImage = (Button)findViewById(R.id.btnUpdateImage);
        btnUpdateName = (Button)findViewById(R.id.btnUpdateName);
        btnUpdateEmail = (Button)findViewById(R.id.btnUpdateEmail);
        btnUpdatePhone = (Button)findViewById(R.id.btnUpdatePhone);
        btnUpdatePassword = (Button)findViewById(R.id.btnUpdatePwd);

        if(firebaseUser!=null){
            email.setText(firebaseUser.getEmail());
            name.setText(firebaseUser.getDisplayName());
            phone.setText(firebaseUser.getPhoneNumber());
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(userImage);
        }else{
            email.setText("test@gmail.com");
            name.setText("Oliver");
            phone.setText("0000111");
        }

        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/jpeg");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                UserProfileChangeRequest picUpdate = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(src)).build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(picUpdate);
            }
        });

        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileChangeRequest nameUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name.getText().toString())
                        .build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(nameUpdate);
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(email.getText().toString());
            }
        });

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().updatePassword(password.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            Uri uri = data.getData();
            String src = uri.getPath();
        }
    }

}