package com.citi.cititransit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Modules.checkService;

/**
 * Created by yianmo on 11/13/17.
 */

public class RouteChoices extends AppCompatActivity implements OnMapReadyCallback {
    private Button payTest;
    private TextView nameTextViewID, infoTextViewID, costInfoID;
    private static final int overview = 0;
    private static final String GOOGLE_API_KEY = "AIzaSyAd7BS-PW5TQSPMebQ5OjJbJWsRuJAYueY";
    private DirectionsRoute route;
    private ArrayList<String> TransitLineName=new ArrayList<>();
    private ArrayList<String> TransitStartStop=new ArrayList<>();
    private ArrayList<String> TransitEndStop=new ArrayList<>();
    private ArrayList<ArrayList<String>> reslist = new ArrayList<>();
    private  String transitmode, transitdetail, transitcost;

    private String origin;
    private String destination;
    private ArrayList<String> commuteModes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routechoices);

        Intent bundle = getIntent();
        route=(DirectionsRoute) bundle.getSerializableExtra("parcel_data");
        TransitLineName= (ArrayList)bundle.getSerializableExtra("TransitLineName");
        TransitStartStop= (ArrayList)bundle.getSerializableExtra("TransitStartStop");
        TransitEndStop= (ArrayList)bundle.getSerializableExtra("TransitEndStop");
        transitmode= (String)bundle.getSerializableExtra("info1");
        transitdetail= (String)bundle.getSerializableExtra("info2");
        transitcost= (String)bundle.getSerializableExtra("info3");

        nameTextViewID=findViewById(R.id.nameTextViewID);
        nameTextViewID.setText(transitmode);
        infoTextViewID=findViewById(R.id.infoTextViewID);
        infoTextViewID.setText(transitdetail);
        costInfoID=findViewById(R.id.costInfoID);
        costInfoID.setText(transitcost);

        payTest = (Button) findViewById(R.id.payTest);

        route=(DirectionsRoute) bundle.getSerializableExtra("parcel_data");
        origin = (String)bundle.getSerializableExtra("start");
        destination = (String)bundle.getSerializableExtra("end");
        commuteModes = (ArrayList<String>)bundle.getSerializableExtra("commuteModes");
        Log.d("test/RouteChoice", "origin: " + origin);
        Log.d("test/RouteChoice", "destination " + destination);
        Log.d("test/RouteChoice", "modes: " + commuteModes.toString());

        if (route!=null)
            Log.i("test", "yes!! successfully passed in routes");

        payTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteChoices.this, PayActivity.class);
                intent.putExtra("start", origin);
                intent.putExtra("end",destination);
                intent.putExtra("modes", commuteModes);
                startActivity(intent);
            }
        });

        route=(DirectionsRoute) bundle.getSerializableExtra("parcel_data");
        TransitLineName= (ArrayList)bundle.getSerializableExtra("TransitLineName");
        TransitStartStop= (ArrayList)bundle.getSerializableExtra("TransitStartStop");
        TransitEndStop= (ArrayList)bundle.getSerializableExtra("TransitEndStop");

        if (route!=null)
            Log.i("test", "yes!! successfully passed in routes");

       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void checkService(GoogleMap mMap){
        if (TransitLineName!=null) {
            for (int i=0; i<TransitLineName.size(); i++)
                Log.i("test", "transitline has " + TransitLineName.get(i));
            for (int i = 0; i < TransitLineName.size(); i++) {
                InputStream inputStream=getResources().openRawResource(R.raw.f_line);
                Log.i("test", TransitStartStop.get(i) + " to " + TransitEndStop.get(i));
                checkService a = new checkService(TransitLineName.get(i), TransitStartStop.get(i), TransitEndStop.get(i), inputStream);
                reslist = a.getReslist();
                if (reslist != null) {
                    Log.i("test","we are getting the result"+Integer.toString(reslist.size()));
                    for (int j = 0; i < reslist.size(); i += 2) {
                        ArrayList<String> startpoint = reslist.get(j);
                        ArrayList<String> endpoint = reslist.get(j + 1);

                        LatLng point1 = new LatLng(Double.parseDouble(startpoint.get(0)), Double.parseDouble(startpoint.get(1)));
                        LatLng point2 = new LatLng(Double.parseDouble(endpoint.get(0)), Double.parseDouble(endpoint.get(1)));
                        List<LatLng> newlist = new ArrayList<LatLng>();
                        newlist.add(point1);
                        newlist.add(point2);

                        //mMap.addPolyline(new PolylineOptions().color(Color.BLACK).width(20).addAll(newlist));

                        DirectionsResult transit=getDirectionsDetails(startpoint.get(0)+","+startpoint.get(1) ,endpoint.get(0)+","+ endpoint.get(1) ,TravelMode.TRANSIT);
                        List<LatLng> changepath = PolyUtil.decode(transit.routes[0].overviewPolyline.getEncodedPath());
                        if (changepath!=null)
                            Log.i("test","yes we are calling api again");
                        mMap.addPolyline(new PolylineOptions().color(getResources().getColor(R.color.draworange)).width(30).addAll(changepath));
                    }
                }
            }
        }
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
        String test=route.overviewPolyline.getEncodedPath();
        Log.i("test", test);
        Log.i("testleg", route.legs[overview].startAddress);
        Log.i("testleg", route.legs[overview].endAddress);
        DirectionsStep[] steps=route.legs[overview].steps;
        for (int i=0;i<steps.length;i++){
            if (steps[i].travelMode== TravelMode.TRANSIT) {
                Log.i("testname", steps[i].transitDetails.line.shortName);
                Log.i("testname", Integer.toString(steps[i].transitDetails.numStops));
                Log.i("testname", steps[i].transitDetails.departureStop.name);
                Log.i("testname", steps[i].transitDetails.departureStop.location.toString());
                Log.i("testname", steps[i].transitDetails.arrivalStop.location.toString());
                Log.i("testname", steps[i].transitDetails.arrivalStop.name);
            }
        }
        LatLng point1= new LatLng(40.759172,-73.953235);
        LatLng point2= new LatLng(40.764618,-73.966090);
        List<LatLng> newlist= new ArrayList<LatLng>();
        newlist.add(point1);
        newlist.add(point2);
        mMap.addPolyline(new PolylineOptions().color(R.color.draworange).width(10).addAll(decodedPath));
        //mMap.addPolyline(new PolylineOptions().color(Color.BLACK).width(10).addAll(newlist));
        checkService(mMap);

    }

    private String getEndLocationTitle(DirectionsRoute route){
        return  "Time :"+ route.legs[overview].duration.humanReadable + " Distance :" + route.legs[overview].distance.humanReadable;
    }

    private DirectionsResult getDirectionsDetails(String origin, String destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .alternatives(true)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext.Builder geoApiContext =new GeoApiContext.Builder();
        return geoApiContext
                .queryRateLimit(3)
                .apiKey("AIzaSyAd7BS-PW5TQSPMebQ5OjJbJWsRuJAYueY")
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS).build();
    }

}
