package com.capstone.dolphindive;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button chatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DiveShop()).commit();
        }
    }

    public void chatButtonClick(View v) {
        Intent myIntent = new Intent(MainActivity.this, Chatting.class);
        // for ex: your package name can be "com.example"
        // your activity name will be "com.example.Contact_Developer"
        startActivity(myIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_community:
                    selectedFragment = new SocialPlatform();
                    break;
                case R.id.nav_diveshop:
                    selectedFragment = new DiveShop();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new Profile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent regintent = new Intent(MainActivity.this, Login.class);
        startActivity(regintent);
        finish();
    }

    ;
}
