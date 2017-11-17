package com.citi.cititransit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PayReceiptActivity extends AppCompatActivity {

    private Button paymentFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_receipt);


        paymentFinishButton = (Button) findViewById(R.id.paymentFinishButton);
        paymentFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayReceiptActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
