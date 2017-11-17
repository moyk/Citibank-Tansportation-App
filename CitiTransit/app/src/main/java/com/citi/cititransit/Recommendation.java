package com.citi.cititransit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Adapters.RecomendationRowAdapter;


/**
 * Created by yianmo on 10/13/17.
 */

public class Recommendation extends AppCompatActivity {

    private Button payTest;
    private static final int overview = 0;
    private static final String GOOGLE_API_KEY = "AIzaSyAd7BS-PW5TQSPMebQ5OjJbJWsRuJAYueY";
    ListView listView;
    private ArrayList<DirectionsRoute> routeArray;
    private ArrayList<ArrayList<String>> TransitNameArray;
    private ArrayList<ArrayList<String>> TransitStartArray ;
    private ArrayList<ArrayList<String>> TransitEndArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_recommendation);

        Bundle bundle = getIntent().getExtras();
        String destination = bundle.getString("destination");
        String origin = bundle.getString("origin");


        Log.i("test", "yes"+ destination);
        Log.i("test", "Yes"+ origin);

        ArrayList<String> nameArray = new ArrayList<String>();

        ArrayList<String> infoArray = new ArrayList<String>();

        ArrayList<Integer> imageArray = new ArrayList<Integer>();

        ArrayList<String> costArray = new ArrayList<String>();

        TransitNameArray = new ArrayList<>();
        TransitStartArray = new ArrayList<>();
        TransitEndArray = new ArrayList<>();

        routeArray=new ArrayList<DirectionsRoute>();

        DirectionsResult bic=getDirectionsDetails(origin,destination,TravelMode.BICYCLING);
        if (bic != null) {
            Log.i("test", bic.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Bicycle");
            infoArray.add(bic.routes[overview].legs[overview].duration.humanReadable);
            //imageArray.add(R.drawable.ic_bicycle);
            costArray.add("free");
            routeArray.add(bic.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());

        }
        DirectionsResult walk=getDirectionsDetails(origin,destination,TravelMode.WALKING);
        if (walk != null) {
            Log.i("test", walk.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Walking");
            infoArray.add(walk.routes[overview].legs[overview].duration.humanReadable);
            //imageArray.add(R.drawable.ic_walking);
            costArray.add("free");
            routeArray.add(walk.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());
        }
        DirectionsResult drive=getDirectionsDetails(origin,destination,TravelMode.DRIVING);
        if (drive != null) {
            Log.i("test", drive.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Driving");
            infoArray.add(drive.routes[overview].legs[overview].duration.humanReadable);
            //imageArray.add(R.drawable.ic_driving);
            costArray.add("free");
            routeArray.add(drive.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());
        }
        //origin="40.6088428089499,-73.9730028152875";
        //destination="40.644272,-73.97972116";

        DirectionsResult transit=getDirectionsDetails(origin,destination,TravelMode.TRANSIT);
        if (transit != null) {
            DirectionsRoute[] routes = transit.routes;
            for (int routeInd = 0; routeInd < routes.length; routeInd++){
                Log.i("test", routes[routeInd].legs[overview].duration.humanReadable);
                nameArray.add("Transit");
                String transitLines = "";
                DirectionsLeg[] legs = transit.routes[routeInd].legs;
                ArrayList<String> TransitLineName= new ArrayList<>();
                ArrayList<String> TransitStartStop= new ArrayList<>();
                ArrayList<String> TransitEndStop= new ArrayList<>();

                for (int i = 0; i < legs.length; i++) {
                    DirectionsStep[] steps = legs[i].steps;
                    for (int j = 0; j < steps.length; j++) {
                        if (steps[j].travelMode == TravelMode.TRANSIT) {
                            transitLines += steps[j].transitDetails.line.shortName;
                            TransitLineName.add(steps[j].transitDetails.line.shortName);
                            TransitStartStop.add(steps[j].transitDetails.departureStop.name);
                            TransitEndStop.add(steps[j].transitDetails.arrivalStop.name);
                        }
                        if (steps[j].travelMode != TravelMode.TRANSIT)
                            transitLines += steps[j].travelMode.toString();
                        if (j < steps.length - 1)
                            transitLines += "->";
                    }
                }
                infoArray.add(transitLines);
                //infoArray.add(drive.routes[overview].legs[overview].duration.humanReadable);
                imageArray.add(R.drawable.ic_driving);
                costArray.add("$2.65");
                TransitNameArray.add(TransitLineName);
                TransitStartArray.add(TransitStartStop);
                TransitEndArray.add(TransitEndStop);

                routeArray.add(routes[routeInd]);
            }
        }

        RecomendationRowAdapter whatever = new RecomendationRowAdapter(this, nameArray, infoArray, costArray, imageArray);
        listView = findViewById(R.id.listviewID);
        listView.setAdapter(whatever);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(Recommendation.this, RouteChoices.class);
                intent.putExtra("parcel_data", routeArray.get(position));
                intent.putExtra("TransitLineName",TransitNameArray.get(position));
                intent.putExtra("TransitStartStop",TransitStartArray.get(position));
                intent.putExtra("TransitEndStop",TransitEndArray.get(position));
                startActivity(intent);
            }
        });
    }


    private DirectionsResult getDirectionsDetails(String origin,String destination,TravelMode mode) {
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
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS).build();
    }



}
