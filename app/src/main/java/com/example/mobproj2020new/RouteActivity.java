package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener{

    private GoogleMap mMap;
    MarkerOptions place1, place2;
    Polyline currentPolyline;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //final Geocoder geocoder2 = new Geocoder(this);

        findViewById(R.id.haeButton).setOnClickListener(this);
        findViewById(R.id.sijaintiButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.haeButton)
        {
            SearchView lahtoEditori = (SearchView) findViewById(R.id.lahtoEdit);
            SearchView loppuEditori = (SearchView) findViewById(R.id.maaranpaaEdit);

            String strLahto = lahtoEditori.getQuery().toString();
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
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                String latitude = String.valueOf(location.getLatitude());
                                String longitude = String.valueOf(location.getLongitude());
                                Toast.makeText(getApplicationContext(),latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Ei sijaintia", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

        place1 = new MarkerOptions().position(new LatLng(startLat, startLon)).title("Location 1");
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
