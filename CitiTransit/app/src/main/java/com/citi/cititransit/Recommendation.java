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
import java.util.HashSet;
import java.util.Set;
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

    private ArrayList<String> nameArray ;

    private ArrayList<String> infoArray;

    private ArrayList<Integer> imageArray;

    private ArrayList<String> costArray;

    private String origin, destination;
    private ArrayList<Set<String>> paymentInfoArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_recommendation);

        Bundle bundle = getIntent().getExtras();
        destination = bundle.getString("destination");
        origin = bundle.getString("origin");

        Log.i("test", "yes"+ destination);
        Log.i("test", "Yes"+ origin);

        nameArray = new ArrayList<String>();

        infoArray = new ArrayList<String>();

        imageArray = new ArrayList<Integer>();

        costArray = new ArrayList<String>();

        TransitNameArray = new ArrayList<>();
        TransitStartArray = new ArrayList<>();
        TransitEndArray = new ArrayList<>();
        paymentInfoArray = new ArrayList();

        routeArray=new ArrayList<DirectionsRoute>();

        DirectionsResult bic=getDirectionsDetails(origin,destination,TravelMode.BICYCLING);
        if (bic != null) {
            Log.i("test", bic.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Bicycle");
            infoArray.add("");
            imageArray.add(R.drawable.ic_driving);
            costArray.add(bic.routes[overview].legs[overview].duration.humanReadable);
            routeArray.add(bic.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());
            Set<String> temp= new HashSet<>();
            temp.add("Bicycling");
            paymentInfoArray.add(temp);


        }
        DirectionsResult walk=getDirectionsDetails(origin,destination,TravelMode.WALKING);
        if (walk != null) {
            Log.i("test", walk.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Walking");
            infoArray.add("");
            imageArray.add(R.drawable.ic_driving);
            costArray.add(walk.routes[overview].legs[overview].duration.humanReadable);
            routeArray.add(walk.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());
            Set<String> temp= new HashSet<>();
            temp.add("walking");
            paymentInfoArray.add(temp);
        }
        DirectionsResult drive=getDirectionsDetails(origin,destination,TravelMode.DRIVING);
        if (drive != null) {
            Log.i("test", drive.routes[overview].legs[overview].duration.humanReadable);
            nameArray.add("Driving");
            infoArray.add("");
            imageArray.add(R.drawable.ic_driving);
            costArray.add(drive.routes[overview].legs[overview].duration.humanReadable);
            routeArray.add(drive.routes[overview]);
            TransitNameArray.add(new ArrayList<String>());
            TransitStartArray.add(new ArrayList<String>());
            TransitEndArray.add(new ArrayList<String>());
            Set<String> temp= new HashSet<>();
            temp.add("driving");
            paymentInfoArray.add(temp);
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
                Set<String> temp= new HashSet<>();
                for (int i = 0; i < legs.length; i++) {
                    DirectionsStep[] steps = legs[i].steps;
                    for (int j = 0; j < steps.length; j++) {
                        if (steps[j].travelMode == TravelMode.TRANSIT) {
                            transitLines += steps[j].transitDetails.line.shortName;
                            TransitLineName.add(steps[j].transitDetails.line.shortName);
                            TransitStartStop.add(steps[j].transitDetails.departureStop.name);
                            TransitEndStop.add(steps[j].transitDetails.arrivalStop.name);
                            temp.add(steps[j].transitDetails.line.vehicle.type.toString());
                        }
                        if (steps[j].travelMode != TravelMode.TRANSIT)
                            transitLines += steps[j].travelMode.toString();
                        if (j < steps.length - 1)
                            transitLines += "->";
                    }
                }
                paymentInfoArray.add(temp);
                infoArray.add(transitLines);
                //infoArray.add(drive.routes[overview].legs[overview].duration.humanReadable);
                imageArray.add(R.drawable.ic_driving);
                costArray.add("$2.65"+"\n"+ transit.routes[overview].legs[overview].duration.humanReadable);
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
                intent.putExtra("info1", nameArray.get(position));
                intent.putExtra("info2", infoArray.get(position));
                intent.putExtra("info3", costArray.get(position));
                intent.putExtra("info4", imageArray.get(position));
                intent.putExtra("parcel_data", routeArray.get(position));
                intent.putExtra("TransitLineName",TransitNameArray.get(position));
                intent.putExtra("TransitStartStop",TransitStartArray.get(position));
                intent.putExtra("TransitEndStop",TransitEndArray.get(position));
                intent.putExtra("start", origin);
                intent.putExtra("end", destination);
                intent.putExtra("commuteModes", new ArrayList<>(paymentInfoArray.get(position)));
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
