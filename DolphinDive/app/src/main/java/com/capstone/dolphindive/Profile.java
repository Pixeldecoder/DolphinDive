package com.capstone.dolphindive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name="None";
        if (user != null) {
            // Name, email address etc
            name = user.getDisplayName();
        }
        Button edit= (Button)view.findViewById(R.id.editProfile);
        Button Divelog = (Button)view.findViewById(R.id.DivelogBtn);
        Button posts = (Button)view.findViewById(R.id.PostsBtn);
        ImageButton logout =(ImageButton)view.findViewById(R.id.logoutBtn);
        TextView userName = (TextView) view.findViewById(R.id.profile_username);
        TextView numPosts = (TextView) view.findViewById(R.id.profile_numPosts);
        TextView numFollowing = (TextView) view.findViewById(R.id.profile_numFol);
        TextView numFollower = (TextView) view.findViewById(R.id.profile_numFollower);
        ImageView portrait = (ImageView)view.findViewById(R.id.profile_portrait);
        edit.setOnClickListener((View.OnClickListener) this);
        Divelog.setOnClickListener((View.OnClickListener) this);
        posts.setOnClickListener((View.OnClickListener) this);
        logout.setOnClickListener((View.OnClickListener) this);
        userName.setText(name);
        numPosts.setText("0");
        numFollowing.setText("0");
        numFollower.setText("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editProfile:
                Intent i = new Intent(getActivity(), ProfileManagement.class);
                startActivity(i);
                break;
            case R.id.DivelogBtn:
                break;
            case R.id.PostsBtn:
                break;
            case R.id.logoutBtn:
                logout();
                break;
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent regintent = new Intent(getActivity(), Login.class);
        startActivity(regintent);
    };
}