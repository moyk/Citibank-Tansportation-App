package com.citi.cititransit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.auth.FirebaseAuth;

import Modules.UserCommuteHistory;

public class PayReceiptActivity extends AppCompatActivity {

    private Button paymentFinishButton;
    private FirebaseAuth mAuth;
    private AmazonDynamoDBClient ddbClient;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private DynamoDBMapper dbMapper;

    private UserCommuteHistory commuteHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_receipt);

        //Obtain commute history from last activity
        Intent bundle = getIntent();
        commuteHistory = (UserCommuteHistory)bundle.getSerializableExtra("commuteHistory");
        paymentFinishButton = (Button) findViewById(R.id.paymentFinishButton);
        mAuth = FirebaseAuth.getInstance();

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-2:37ce01bb-3584-4401-a95f-6046de7975f8", // Identity pool ID
                Regions.US_EAST_2 // Region
        );
        ddbClient = Region.getRegion(Regions.US_EAST_2).createClient(
                AmazonDynamoDBClient.class,
                credentialsProvider,
                new ClientConfiguration()
        );
        dbMapper = DynamoDBMapper.builder().dynamoDBClient(ddbClient).build();

        paymentFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayReceiptActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        saveUserCommuteHistory();
    }

    private void saveUserCommuteHistory(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dbMapper.save(commuteHistory);
            }
        };
        Thread dbThread = new Thread(runnable);
        dbThread.start();
    }
}
