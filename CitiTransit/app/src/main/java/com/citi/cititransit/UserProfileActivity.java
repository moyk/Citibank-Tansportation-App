package com.citi.cititransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.firebase.auth.FirebaseAuth;

import Modules.User;

public class UserProfileActivity extends AppCompatActivity {

    //DynamoDB
    private AmazonDynamoDBClient ddbClient;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private DynamoDBMapper dbMapper;
    private FirebaseAuth mAuth;
    private TextView userName;
    private TextView email;
    private FirebaseAuth firebaseAuth;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        firebaseAuth = FirebaseAuth.getInstance();

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

        userName = (TextView) findViewById(R.id.user_profile_name);
        email = (TextView) findViewById(R.id.userProfEmail);

        mAuth = FirebaseAuth.getInstance();
        //if current page is first time load in then perform a load from database
        if(currentUser == null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //load from db if it is null, generate a new instance for this user
                    currentUser = dbMapper.load(User.class, mAuth.getCurrentUser().getUid());
                    if (currentUser == null) {
                        currentUser = newUserGen(mAuth.getCurrentUser().getEmail(), firebaseAuth.getCurrentUser().getUid());
                        Log.d("UserSignUpActivity", currentUser.getUserId().toString());
                        dbMapper.save(currentUser);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userName.setText(currentUser.getUserName());
                            email.setText(currentUser.getUserEmail());
                        }
                    });

                }
            };
            Thread dbThread = new Thread(runnable);
            dbThread.start();
        }
    }

    private User newUserGen(String email, String firebaseUserId){
        User user = new User();
        user.setUserEmail(email);
        user.setUserId(firebaseUserId);
        user.setUserName(email.split("@")[0]);
        return user;
    }

}

