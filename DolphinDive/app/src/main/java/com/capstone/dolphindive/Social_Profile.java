package com.capstone.dolphindive;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.capstone.dolphindive.utility.CircleTransform;
import com.capstone.dolphindive.utility.UserProfile;
import com.capstone.dolphindive.utility.UserProfileCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Social_Profile extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    Button followBtn;
    TextView userName;
    TextView numPosts;
    TextView numFollowing ;
    TextView numFollower;
    ImageView portrait;
    String cur_uid;
    String target_uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_profile);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        cur_uid = user.getUid();
        Intent intent = getIntent();
        //Use following to pass the targeting user Uid to the activity
        target_uid = intent.getStringExtra("uid");
        documentReference = db.collection("Users").document(cur_uid);

        followBtn= (Button)findViewById(R.id.followBtn);
        userName = (TextView) findViewById(R.id.profile_username);
        numPosts = (TextView) findViewById(R.id.profile_numPosts);
        numFollowing = (TextView) findViewById(R.id.profile_numFol);
        numFollower = (TextView) findViewById(R.id.profile_numFollower);
        portrait = (ImageView)findViewById(R.id.profile_portrait);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference following = documentReference.collection("following").document(target_uid);
                DocumentReference follower  = db.collection("Users").document(target_uid).collection("follower").document(cur_uid);
                Map<String, Object> fileds = new HashMap<>();
                fileds.put("exist", true);
                following.set(fileds).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        follower.set(fileds).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Social_Profile.this, "Successfully Followed",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Social_Profile.this, "Following Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        UserProfile userProfile = new UserProfile(target_uid);
        userProfile.getProfile(new UserProfileCallback() {
            @Override
            public void onComplete(HashMap<String,String> profile) {
                if(profile!=null){
                    String name = profile.get("name");
                    String numFollower_text = profile.get("numFollower");
                    String numFollowing_text = profile.get("numFollowing");
                    String numPosts_text = profile.get("numPosts");
                    String url = profile.get("url");
                    if(!TextUtils.isEmpty(url)){
                        Picasso.get().load(url).transform(new CircleTransform()).centerCrop().fit().into(portrait);
                        userName.setText(name);
                        numFollower.setText(numFollower_text);
                        numFollowing.setText(numFollowing_text);
                        numPosts.setText(numPosts_text);

                    }else{
                        userName.setText(name);
                        numFollower.setText(numFollower_text);
                        numFollowing.setText(numFollowing_text);
                        numPosts.setText(numPosts_text);
                    }
                }else{
                    Toast.makeText(Social_Profile.this, "No Profile Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}