package com.capstone.dolphindive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.capstone.dolphindive.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Follower_List extends AppCompatActivity {

    ImageButton back;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list);

//        back = findViewById(R.id.back_arrow);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent logintent = new Intent(Follower_List.this, Profile.class);
//                startActivity(logintent);
//            }
//
//        });

    }
}