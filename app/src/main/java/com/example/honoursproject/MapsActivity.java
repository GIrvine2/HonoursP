package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();
    ArrayList<LatLng> waypoints = new ArrayList<LatLng>();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference confirmedOrder = db.getReference().child("Basket").child(userID).child("Locations");

    private GoogleMap mMap;
    Button cancelBtn;

    MarkerOptions origin, destination;

    //String for waypoints URL
    String str_wp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        cancelBtn = findViewById(R.id.cancelBtn);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        origin = new MarkerOptions().position(new LatLng(57.149651, -2.099075)).title("Drivers Location");
        destination = new MarkerOptions().position(new LatLng(57.3646, -2.0730)).title("Your Location");


        confirmedOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                double lat = (double) snapshot.child("latitude").getValue();
                double lng = (double) snapshot.child("longitude").getValue();

                LatLng location = new LatLng(lat, lng);
                waypoints.add(location);
                System.out.println(waypoints);

                String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                System.out.println(url);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {


        /**
         * Checking how many restaurants the user has ordered from
         * Solution code here is not great however after difficulties with another solution this was the only option
         * Also because the URL needs waypoints in a specific way to process the request
         */
        if(waypoints.size() == 0 ) {
            System.out.println("EQUAL 0 ");
        }
        if(waypoints.size() == 1) {
            str_wp = "waypoints=" + waypoints.get(0).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "");
            System.out.println(str_wp);
        }
        if(waypoints.size() == 2) {
            str_wp = "waypoints="+waypoints.get(0).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
            + "|" + waypoints.get(1).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "");
            System.out.println(str_wp);
        }
        if(waypoints.size() == 3) {
            str_wp = "waypoints="+waypoints.get(0).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(1).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(2).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "");
            System.out.println(str_wp);
        }
        if(waypoints.size() == 4) {
            str_wp = "waypoints="+waypoints.get(0).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(1).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(2).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(3).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "");
            System.out.println(str_wp);
        }
        if(waypoints.size() == 5) {
            str_wp = "waypoints="+waypoints.get(0).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(1).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(2).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(3).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "")
                    + "|" + waypoints.get(4).toString().replace("lat/lng", "").replace(":", "").replace("(", "").replace(")", "").replace(" ", "");
            System.out.println(str_wp);
        }


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        // Setting mode
        String mode = "mode=driving";

        String optimize = "optimizeWaypoints=true";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_wp + "&" + optimize + "&" + mode + "&";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyB09HSX1CZ0kBXHRe0HyIAHPHMyNu812L4";

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map
            System.out.println(points.size());
            if (points.size() != 0) {
                mMap.addPolyline(lineOptions);
            }


        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(origin);
        mMap.addMarker(destination);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination.getPosition(), 15));
    }

}