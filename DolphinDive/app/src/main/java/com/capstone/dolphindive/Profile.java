package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment implements View.OnClickListener{

    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference documentReference;
    Button edit;
    Button Divelog;
    Button posts;
    ImageButton logout;
    TextView userName;
    TextView numPosts;
    TextView numFollowing ;
    TextView numFollower;
    ImageView portrait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db=  FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        documentReference = db.collection("user").document("profile");

        String name="None";
        if (user != null) {
            // Name, email address etc
            name = user.getDisplayName();
        }
        edit= (Button)view.findViewById(R.id.editProfile);
        Divelog = (Button)view.findViewById(R.id.DivelogBtn);
        posts = (Button)view.findViewById(R.id.PostsBtn);
        logout =(ImageButton)view.findViewById(R.id.logoutBtn);
        userName = (TextView) view.findViewById(R.id.profile_username);
        numPosts = (TextView) view.findViewById(R.id.profile_numPosts);
        numFollowing = (TextView) view.findViewById(R.id.profile_numFol);
        numFollower = (TextView) view.findViewById(R.id.profile_numFollower);
        portrait = (ImageView)view.findViewById(R.id.profile_portrait);
        edit.setOnClickListener((View.OnClickListener) this);
        Divelog.setOnClickListener((View.OnClickListener) this);
        posts.setOnClickListener((View.OnClickListener) this);
        logout.setOnClickListener((View.OnClickListener) this);
        numPosts.setText("0");
        numFollowing.setText("0");
        numFollower.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editProfile:
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            Intent i = new Intent(getActivity(), ShowProfile.class);
                            startActivity(i);
                        }else{
                            Intent i = new Intent(getActivity(), EditProfile.class);
                            Bundle bundle =new Bundle();
                            bundle.putString("mode","create");
                            i.putExtras(bundle);
                            startActivity(i);
                        }
                    }
                });
                break;
            case R.id.DivelogBtn:
                Intent myIntent = new Intent(getActivity(), DiveLog_Scu.class);
                startActivity(myIntent);
                break;
            case R.id.PostsBtn:
                break;
            case R.id.logoutBtn:
                logout();
                break;
        }
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
                    if(!TextUtils.isEmpty(name) ){
                        userName.setText(name);
                    }else{
                        userName.setText(user.getEmail());
                    }
                    if(url!=null){
                        Picasso.get().load(url).centerCrop().fit().into(portrait);
                    }

                }else{
                    Toast.makeText(getActivity(), "No Profile Exist",Toast.LENGTH_SHORT).show();
                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent regintent = new Intent(getActivity(), Login.class);
        startActivity(regintent);
    };
}