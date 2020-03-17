package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback{

    private GoogleMap mMap;
    MarkerOptions place1, place2;
    Polyline currentPolyline;
    Button haeReitti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        haeReitti = findViewById(R.id.haeButton);
        haeReitti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lahtoEditori = (EditText) findViewById(R.id.lahtoEdit);
                String strLahto = lahtoEditori.getText().toString();
                Double lahtoPiste = Double.parseDouble(strLahto);
                if (strLahto.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Valitse lahtö- tai loppupiste", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    place1 = new MarkerOptions().position(new LatLng(lahtoPiste, 25.478431)).title("Location 1");
                    place2 = new MarkerOptions().position(new LatLng(63.68855, 25.978137)).title("Location 2");
                    new FetchURL(RouteActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                    mMap.addMarker(place1);
                    mMap.addMarker(place2);

                }

            }
        });

        //origin=65.027,25.478&destination=63.688,25.978
        //String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng oulu = new LatLng(-34.364, 147.891);
       // mMap.addMarker(new MarkerOptions().position(oulu).title("Marker in Oulu"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(oulu));

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
    }
}
