package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.dolphindive.utility.CircleTransform;
import com.capstone.dolphindive.utility.UserProfile;
import com.capstone.dolphindive.utility.UserProfileCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ShowProfile extends AppCompatActivity {

    FloatingActionButton btn;
    FloatingActionButton back_btn;
    ImageView imageView;
    TextView sh_name, sh_email, sh_DiverId, sh_phone, sh_address;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    DocumentReference documentReference;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        sh_name = findViewById(R.id.name_profileShow);
        sh_email = findViewById(R.id.email_profileShow);
        sh_DiverId = findViewById(R.id.diverID_profileShow);
        sh_phone = findViewById(R.id.phone_profileShow);
        sh_address = findViewById(R.id.address_profileShow);
        btn = findViewById(R.id.floating_btn_profileShow);
        back_btn = findViewById(R.id.back_btn_profileShow);
        imageView = findViewById(R.id.image_view_profileShow);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        documentReference = db.collection("Users").document(uid);
        storageReference = firebaseStorage.getInstance().getReference("Profile Images");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowProfile.this, EditProfile.class);
                Bundle bundle =new Bundle();
                bundle.putString("mode","edit");
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserProfile userProfile = new UserProfile(uid);
        userProfile.getProfile(new UserProfileCallback() {
            @Override
            public void onComplete(HashMap<String,String> profile) {
                if(profile!=null){
                    String name = profile.get("name");
                    String email = profile.get("email");
                    String phone = profile.get("phone");
                    String address = profile.get("address");
                    String diverId = profile.get("diverId");
                    String url = profile.get("url");
                    if(!TextUtils.isEmpty(url)){
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(imageView);
                        sh_name.setText(name);
                        sh_email.setText(email);
                        sh_phone.setText(phone);
                        sh_address.setText(address);
                        sh_DiverId.setText(diverId);
                    }else{
                        sh_name.setText(name);
                        sh_email.setText(email);
                        sh_phone.setText(phone);
                        sh_address.setText(address);
                        sh_DiverId.setText(diverId);
                    }
                }else{
                    Toast.makeText(ShowProfile.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.getResult().exists()){
//                    String name = task.getResult().getString("name");
//                    String email = task.getResult().getString("email");
//                    String phone = task.getResult().getString("phone");
//                    String address = task.getResult().getString("address");
//                    String diverId = task.getResult().getString("diverId");
//                    String url = task.getResult().getString("url");
//                    if(!TextUtils.isEmpty(url)){
//                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(imageView);
//                        sh_name.setText(name);
//                        sh_email.setText(email);
//                        sh_phone.setText(phone);
//                        sh_address.setText(address);
//                        sh_DiverId.setText(diverId);
//                    }else{
//                        sh_name.setText(name);
//                        sh_email.setText(email);
//                        sh_phone.setText(phone);
//                        sh_address.setText(address);
//                        sh_DiverId.setText(diverId);
//                    }
//                }else{
//                    Toast.makeText(ShowProfile.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
    }
}