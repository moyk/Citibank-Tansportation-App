package com.citi.cititransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.CommuteHistoryRowAdapter;
import Modules.UserCommuteHistory;

public class ProfileHistoryActivity extends AppCompatActivity {

    private AmazonDynamoDBClient ddbClient;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private DynamoDBMapper dbMapper;
    private FirebaseAuth mAuth;
    private List<UserCommuteHistory> userCommuteHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_history);

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

        findCommuteHistoryWithUID(mAuth.getCurrentUser().getUid());
        //Assume the user is logged in

        CommuteHistoryRowAdapter historyRowAdapter = new CommuteHistoryRowAdapter(this,
                R.layout.commute_history_row, this.userCommuteHistory);
        //ListView historyList = (ListView)findViewById(R.id.historyList);

    }

    private void findCommuteHistoryWithUID(final String fireBaseUserId){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
                eav.put("val1", new AttributeValue().withS(fireBaseUserId));
                DynamoDBScanExpression scanExpression =
                        new DynamoDBScanExpression()
                                .withFilterExpression("FirebaseUserId = :val1")
                                .withExpressionAttributeValues(eav);
                userCommuteHistory = dbMapper.scan(UserCommuteHistory.class, scanExpression);
            }
        };
        Thread dbThread = new Thread(runnable);
        dbThread.start();
    }

}
