package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentMethod extends AppCompatActivity {
    private TextView diveshopname,address,adult,children,rooms,checkin,checkout,cellphone,email,policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        Bundle extras = getIntent().getExtras();
        String[] passingarray = extras.getStringArray("Key");
        String[] userfilter = extras.getStringArray("userfilter");
        diveshopname = (TextView) findViewById(R.id.pay_diveshop);
        diveshopname.setText(passingarray[0]);
        address = (TextView) findViewById(R.id.pay_address);
        address.setText(passingarray[11]);
        //price = (TextView) findViewById(R.id.detail_price);
        //price.setText(passingarray[2]);
        cellphone = (TextView) findViewById(R.id.pay_cell);
        cellphone.setText(passingarray[6]);
        email = (TextView) findViewById(R.id.pay_email);
        email.setText(passingarray[7]);
        policy = (TextView) findViewById(R.id.pay_policy);
        policy.setText(passingarray[12]);
        checkin = (TextView) findViewById(R.id.pay_checkin_date);
        checkin.setText(userfilter[0]);
        checkout = (TextView) findViewById(R.id.pay_checkout_date);
        checkout.setText(userfilter[1]);
        adult = (TextView) findViewById(R.id.pay_adult_number);
        adult.setText(userfilter[2]);
        children = (TextView) findViewById(R.id.pay_child_number);
        children.setText(userfilter[3]);
        rooms = (TextView) findViewById(R.id.pay_rooms);
        rooms.setText(userfilter[4]);



        Button confirmation = findViewById(R.id.confirm_booking);
        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmmessage = new Intent(PaymentMethod.this, PaymentConfirmation.class);
                startActivity(confirmmessage);
            }
        });
    }
}