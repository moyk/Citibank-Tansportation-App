package com.citi.cititransit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsRoute;

import java.util.List;

/**
 * Created by yianmo on 11/13/17.
 */

public class RouteChoices extends AppCompatActivity implements OnMapReadyCallback {
    private Button payTest;
    private static final int overview = 0;
    private static final String GOOGLE_API_KEY = "AIzaSyAd7BS-PW5TQSPMebQ5OjJbJWsRuJAYueY";
    private DirectionsRoute route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routechoices);

        payTest = (Button) findViewById(R.id.payTest);
        payTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteChoices.this, PayActivity.class);
                startActivity(intent);
            }
        });

        Intent bundle = getIntent();
        route=(DirectionsRoute) bundle.getSerializableExtra("parcel_data");
        if (route!=null)
            Log.i("test", "yes!! successfully passed in routes");

       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        setupGoogleMapScreenSettings(googleMap);
        if (route != null) {
            addPolyline(googleMap);
            positionCamera(route, googleMap);
            addMarkersToMap(googleMap);
        }
    }

    private void setupGoogleMapScreenSettings(GoogleMap mMap) {
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        //mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    private void addMarkersToMap(GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(route.legs[overview].startLocation.lat,route.legs[overview].startLocation.lng)).title(route.legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(route.legs[overview].endLocation.lat,route.legs[overview].endLocation.lng)).title(route.legs[overview].startAddress).snippet(getEndLocationTitle(route)));
    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.legs[overview].startLocation.lat, route.legs[overview].startLocation.lng), 15));
    }

    private void addPolyline(GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(route.overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().color(Color.BLUE).width(10).addAll(decodedPath));
    }

    private String getEndLocationTitle(DirectionsRoute route){
        return  "Time :"+ route.legs[overview].duration.humanReadable + " Distance :" + route.legs[overview].distance.humanReadable;
    }

}
