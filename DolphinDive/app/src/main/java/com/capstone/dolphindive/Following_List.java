package com.capstone.dolphindive;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Following_List extends AppCompatActivity {

    ImageButton back;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_list);

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