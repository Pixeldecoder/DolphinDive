package com.capstone.dolphindive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;

public class SocialPlatform extends Fragment {
        @Nullable

        private ImageView appName;
        private ImageView appIcon;
        private RecyclerView postList;
        private ImageButton Notification;
        private ImageButton AddNewPostButton;
        View view;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.activity_social_platform, container, false);

            AddNewPostButton = (ImageButton) view.findViewById(R.id.add_new_post);
            AddNewPostButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent addNewPostIntent = new Intent(getActivity(), PostActivity.class);
                    startActivity(addNewPostIntent);
                }
            });

            Notification = (ImageButton) view.findViewById(R.id.notification);
            Notification.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    getParentFragmentManager().beginTransaction().replace(R.id.post_container,
                            new NotifyActivity()).commit();
                }
//                public void onBackPressed(View v){
//                    getParentFragmentManager().beginTransaction().replace(R.id.post_container,
//                            new SocialPlatform()).commit();
//                }
            });
            return view;
        }
    }