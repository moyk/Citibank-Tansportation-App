package com.citi.cititransit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by yianmo on 10/13/17.
 */

public class Recommendation extends AppCompatActivity {

    private Button payTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_recommendation);

        payTest = (Button) findViewById(R.id.payTest);
        payTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recommendation.this, PayActivity.class);
                startActivity(intent);
            }
        });


    }
}
