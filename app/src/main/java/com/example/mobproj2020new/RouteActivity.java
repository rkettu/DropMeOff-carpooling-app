package com.example.mobproj2020new;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener{

    ArrayList<RideDetailPart> fullDetails = new ArrayList<>();

    int INTENT_ID = 8976;
    private GoogleMap mMap;
    MarkerOptions place1, place2;
    Polyline currentPolyline;
    private FusedLocationProviderClient fusedLocationClient;
    private String latitude;
    private String longitude;
    private String strLahto;
    private SearchView lahtoEditori;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setEnabled(false);

        findViewById(R.id.haeButton).setOnClickListener(this);
        findViewById(R.id.sijaintiButton).setOnClickListener(this);
        findViewById(R.id.nextBtn).setOnClickListener(this);

        lahtoEditori = (SearchView) findViewById(R.id.lahtoEdit);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.haeButton)
        {

            hideKeyboard(RouteActivity.this);

            SearchView loppuEditori = (SearchView) findViewById(R.id.maaranpaaEdit);

            strLahto = lahtoEditori.getQuery().toString();
            String strLoppu = loppuEditori.getQuery().toString();

            if (strLahto.trim().equals("") || strLoppu.trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Valitse lahtö- ja loppupiste", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    geoLocate(strLahto, strLoppu);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else if (v.getId() == R.id.sijaintiButton)
        {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                final Geocoder geocoder = new Geocoder(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if( location != null ){
                                    latitude = String.valueOf(location.getLatitude());
                                    longitude = String.valueOf(location.getLongitude());

                                    try{
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        String address = addresses.get(0).getAddressLine(0);
                                        String kaupunki = addresses.get(0).getLocality();
                                        lahtoEditori.setQuery(address, false);
                                    }
                                    catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Ei sijaintia", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        else if (v.getId() == R.id.nextBtn)
        {
            Intent details = new Intent(this, RideDetailsActivity.class);
            startActivityForResult(details, INTENT_ID);
        }

    }


    public void geoLocate(String start, String stop) throws IOException {
        mMap.clear();

        Geocoder gc = new Geocoder(this);

        List<Address> listStart = gc.getFromLocationName(start, 1);
        Address add = listStart.get(0);

        List<Address> listStop = gc.getFromLocationName(stop, 1);
        Address add2 = listStop.get(0);

        double startLat = add.getLatitude();
        double startLon = add.getLongitude();

        double stopLat = add2.getLatitude();
        double stopLon = add2.getLongitude();

        if(latitude != null && !latitude.isEmpty()){
            Double gpsLat = Double.valueOf(latitude);
            Double gpsLon = Double.valueOf(longitude);
            place1 = new MarkerOptions().position(new LatLng(gpsLat, gpsLon)).title("Location 1");
            latitude = null;
            longitude = null;
        }
        else{
            place1 = new MarkerOptions().position(new LatLng(startLat, startLon)).title("Location 1");
        }

        place2 = new MarkerOptions().position(new LatLng(stopLat, stopLon)).title("Location 2");

        new FetchURL(RouteActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        mMap.addMarker(place1);
        mMap.addMarker(place2);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(place1.getPosition()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place1.getPosition(),10));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng suomi = new LatLng(65.55, 25.55);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(suomi));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(suomi, 5));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode){
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        //String parameters = "origin=address=kaarnatie%205" + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!=null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);

        TextView distance = (TextView) findViewById(R.id.testiTxt);
        distance.setText(Constant.DISTANCE + " " + Constant.DURATION);


        Button nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.setEnabled(true);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) { v = new View(activity); }
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_ID && resultCode == Activity.RESULT_OK)
        {

            RideDetailPart newPart = (RideDetailPart) data.getSerializableExtra("DETAILS");
            fullDetails.add(newPart);

            String date = newPart.date;
            String time = newPart.time;
            int passenger = newPart.passenger;
            float price = newPart.price;

            TextView teksti = (TextView) findViewById(R.id.testailua);
            teksti.setText(date + time + passenger + price);

        }
    }
}
