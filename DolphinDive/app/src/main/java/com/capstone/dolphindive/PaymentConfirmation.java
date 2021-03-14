package com.capstone.dolphindive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        Button paymentconfirm = findViewById(R.id.Pay_confirm);
        paymentconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent confirmmessage = new Intent(PaymentConfirmation.this, MainActivity.class);
                startActivity(confirmmessage);
            }
        });
    }
}