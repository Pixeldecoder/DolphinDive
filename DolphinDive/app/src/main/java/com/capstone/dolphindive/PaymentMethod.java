package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
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