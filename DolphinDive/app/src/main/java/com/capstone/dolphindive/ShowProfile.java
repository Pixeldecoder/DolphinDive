package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ShowProfile extends AppCompatActivity {

    FloatingActionButton btn;
    FloatingActionButton back_btn;
    ImageView imageView;
    TextView sh_name, sh_email, sh_DiverId, sh_phone, sh_address;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

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

        documentReference = db.collection("user").document("profile");
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

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String email = task.getResult().getString("email");
                    String phone = task.getResult().getString("phone");
                    String address = task.getResult().getString("address");
                    String diverId = task.getResult().getString("diverId");
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).centerCrop().fit().into(imageView);
                    sh_name.setText(name);
                    sh_email.setText(email);
                    sh_phone.setText(phone);
                    sh_address.setText(address);
                    sh_DiverId.setText(diverId);

                }else{
                    Toast.makeText(ShowProfile.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}