package com.capstone.dolphindive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.capstone.dolphindive.ui.main.SectionsPagerAdapter;
import com.capstone.dolphindive.utility.CircleTransform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.zip.DataFormatException;

public class Follower_List extends AppCompatActivity {


    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    CollectionReference collectionReference;
    TextView name1;
    TextView name2;

    ImageView profile1;
    ImageView profile2;

    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = "SSPXVDqkBCWLPn3fnmwudl8gY2Z2";
        documentReference = db.collection("Users").document(uid);
        collectionReference = db.collection("Users").document(uid).collection("follower");

        name1 = findViewById(R.id.name1);
        profile1 = findViewById(R.id.profile1);

        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Follower_List.this,Social_Profile.class);
                intent.putExtra("uid",uid);
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
                    Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(profile1);

                }else{
                    Toast.makeText(Follower_List.this, "Empty List",Toast.LENGTH_SHORT).show();
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