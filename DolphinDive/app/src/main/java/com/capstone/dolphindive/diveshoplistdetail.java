package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class diveshoplistdetail extends AppCompatActivity {
    private TextView diveshopname,area,price,rate,cellphone,email,about;
    private ImageView image;
    private Integer total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diveshopdetail);
        Button book_trip = findViewById(R.id.book_the_trip);
        ImageButton go_back = findViewById(R.id.go_back_arrow);
        Bundle extras = getIntent().getExtras();
        String[] passingarray = extras.getStringArray("Key");
        String[] userfilter = extras.getStringArray("userfilter");
        diveshopname = (TextView) findViewById(R.id.detail_diveshopname);
        diveshopname.setText(passingarray[0]);
        area = (TextView) findViewById(R.id.detail_area);
        area.setText(passingarray[1]);
        price = (TextView) findViewById(R.id.detail_price);
        price.setText(passingarray[2]);
        rate = (TextView) findViewById(R.id.detail_rate);
        rate.setText(passingarray[3]);
        image = (ImageView) findViewById(R.id.detail_image);
        image.setImageResource(Integer.parseInt(passingarray[5]));
        cellphone = (TextView) findViewById(R.id.detail_cellphone);
        cellphone.setText(passingarray[6]);
        email = (TextView) findViewById(R.id.detail_email);
        email.setText(passingarray[7]);
        about = (TextView) findViewById(R.id.detail_about);
        about.setText(passingarray[8]);
        String singleprice = passingarray[2].replace("$", "");
        total_price = Integer.parseInt(userfilter[5])*Integer.parseInt(singleprice)*(Integer.parseInt(userfilter[2])+Integer.parseInt(userfilter[3]));
        book_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent booktrip = new Intent(diveshoplistdetail.this, PaymentMethod.class);
                booktrip.putExtra("Key", passingarray);
                booktrip.putExtra("userfilter", userfilter);
                booktrip.putExtra("totalprice",total_price);
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