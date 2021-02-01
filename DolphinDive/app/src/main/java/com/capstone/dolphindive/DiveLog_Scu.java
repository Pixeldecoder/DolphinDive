package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class DiveLog_Scu extends AppCompatActivity {

    Button nav_free;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_log_scuba);

        nav_free = findViewById(R.id.free_on);
        cancel = findViewById(R.id.cancel_scuba);

        nav_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
                Intent freeintent = new Intent(DiveLog_Scu.this, DiveLog_Free.class);
                startActivity(freeintent);
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}