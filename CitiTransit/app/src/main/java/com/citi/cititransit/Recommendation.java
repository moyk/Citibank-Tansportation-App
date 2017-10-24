package com.citi.cititransit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;


/**
 * Created by yianmo on 10/13/17.
 */

public class Recommendation extends AppCompatActivity implements DirectionFinderListener {

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

        Bundle bundle = getIntent().getExtras();
        String distance = bundle.getString("distance");
        String duration = bundle.getString("duration");
        String destination = bundle.getString("destination");
        String origin = bundle.getString("origin");

        Log.i("test", distance);
        Log.i("test", duration);
        Log.i("test", destination);
        Log.i("test", origin);

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        for (Route route : routes) {
            Log.i("test", "yes!!!");
        }
    }
}
