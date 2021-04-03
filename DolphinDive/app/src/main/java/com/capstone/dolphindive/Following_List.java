package com.capstone.dolphindive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

//import com.capstone.dolphindive.model.FollowModel;
import com.capstone.dolphindive.utility.CircleTransform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Following_List extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    DocumentReference documentReference2;

    CollectionReference collectionReference;
    TextView name1;
    TextView name2;

    ImageView profile1;
    ImageView profile2;

    String uid;
    String uid2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_list);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = "DIX9JIIFFWRVRb6HqUfduEdDB3r1";
        uid2 = "Mz459IRSlfgGZMdTVmtMbnnFSUq2";
        documentReference = db.collection("Users").document(uid);
        documentReference2 = db.collection("Users").document(uid2);

        collectionReference = db.collection("Users").document(uid).collection("following");

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        profile1 = findViewById(R.id.profile1);
        profile2 = findViewById(R.id.profile2);

        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Following_List.this,Social_Profile.class);
                intent.putExtra("uid",uid);
                startActivity(intent);

            }

        });

        profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Following_List.this,Social_Profile.class);
                intent.putExtra("uid",uid2);
                startActivity(intent);

            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String url = task.getResult().getString("url");

                    name1.setText(name);
                    if (!TextUtils.isEmpty(url)) {
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(profile1);
                    }

                }else{
                    Toast.makeText(Following_List.this, "Empty List",Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    String url = task.getResult().getString("url");

                    name2.setText(name);
                    if (!TextUtils.isEmpty(url)) {
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(profile2);
                    }

                }else{
                    Toast.makeText(Following_List.this, "Empty List",Toast.LENGTH_SHORT).show();
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