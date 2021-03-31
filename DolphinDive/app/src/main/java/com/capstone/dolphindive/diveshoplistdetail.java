package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;

public class diveshoplistdetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diveshopdetail);
        Button book_trip = findViewById(R.id.book_the_trip);
        ImageButton go_back = findViewById(R.id.go_back_arrow);
        book_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent booktrip = new Intent(diveshoplistdetail.this, PaymentMethod.class);
                startActivity(booktrip);
            }
        });

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

}