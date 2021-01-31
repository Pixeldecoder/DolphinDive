package com.capstone.dolphindive;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class DiveLog_Free extends AppCompatActivity {

    Button nav_scu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_log_free);

        nav_scu = findViewById(R.id.scuba_off);

        nav_scu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Login.class));
                Intent freeintent = new Intent(DiveLog_Free.this, DiveLog_Scu.class);
                startActivity(freeintent);
            }

        });

    }

}