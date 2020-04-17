package com.example.mobproj2020new;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, View.OnClickListener{

    ArrayList<RideDetailPart> fullDetails = new ArrayList<>();

    int INTENT_ID = 8976;
    private GoogleMap mMap;
    MarkerOptions place1, place2, wayPoint1, wayPoint2;
    Polyline currentPolyline;
    private FusedLocationProviderClient fusedLocationClient;
    private String latitude;
    private String longitude;
    private String strLahto, strWaypoint1, strWaypoint2, strLoppu, strLahtoCity, strLoppuCity;
    private String matka;
    private SearchView lahtoEditori, etappiEditori, etappiEditori2, loppuEditori;
    private Button nextBtn;
    private String aika;
    private ImageButton etappiRemoveBtn, etappiRemoveBtn2;
    private int lukitus = 0;

    Animation ttbAnim, bttAnim;
    private LinearLayout linearContainer;
    private ConstraintLayout routeDetails;
    private Button drawerButton;
    private ImageButton sijaintiButton;
    private boolean drawer_expand = true;

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
        findViewById(R.id.nextBtn).setOnClickListener(this);
        findViewById(R.id.etappiBtn).setOnClickListener(this);
        sijaintiButton = findViewById(R.id.sijaintiButton);
        sijaintiButton.setOnClickListener(this);

        etappiRemoveBtn = (ImageButton)findViewById(R.id.etappiRemoveBtn);
        etappiRemoveBtn2 = (ImageButton)findViewById(R.id.etappiRemoveBtn2);

        etappiRemoveBtn.setOnClickListener(this);
        etappiRemoveBtn2.setOnClickListener(this);

        lahtoEditori = (SearchView) findViewById(R.id.lahtoEdit);
        etappiEditori = (SearchView) findViewById(R.id.etappiEdit);
        etappiEditori2 = (SearchView) findViewById(R.id.etappiEdit2);

        ttbAnim = new AnimationUtils().loadAnimation(this, R.anim.toptobottomanimation);
        bttAnim = new AnimationUtils().loadAnimation(this, R.anim.bottomtotopanimation);

        linearContainer = (LinearLayout) findViewById(R.id.linearLayout);
        drawerButton = (Button) findViewById(R.id.drawer_bottom);

        routeDetails = (ConstraintLayout) findViewById(R.id.routeDetails);

        bttAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void expandableDrawer(View view) {
        animationHandler();
    }

    private void animationHandler(){
        if (!drawer_expand){
            linearContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            doAnimation(ttbAnim);
            drawer_expand = true;
        }else{
            doAnimation(bttAnim);
            drawer_expand = false;
        }
    }

    private void doAnimation(Animation anim){
        linearContainer.startAnimation(anim);
        drawerButton.startAnimation(anim);
        sijaintiButton.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.haeButton)
        {
            wayPoint1 = null;
            wayPoint2 = null;
            hideKeyboard(RouteActivity.this);

            loppuEditori = (SearchView) findViewById(R.id.maaranpaaEdit);

            strLahto = lahtoEditori.getQuery().toString();
            strLoppu = loppuEditori.getQuery().toString();
            strWaypoint1 = etappiEditori.getQuery().toString();
            strWaypoint2 = etappiEditori2.getQuery().toString();
            Constant.waypointAddressesList.set(0, strWaypoint1);
            Constant.waypointAddressesList.set(1, strWaypoint2);

            if (strLahto.trim().equals("") || strLoppu.trim().equals(""))
            {
                Toast.makeText(getApplicationContext(),"Choose start and destination points", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    geoLocate(strLahto, strLoppu, strWaypoint1, strWaypoint2);
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
                                    //latitude = String.valueOf(location.getLatitude());
                                    //longitude = String.valueOf(location.getLongitude());

                                    try{
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        String address = addresses.get(0).getAddressLine(0);
                                        //String kaupunki = addresses.get(0).getLocality();
                                        lahtoEditori.setQuery(address, false);
                                    }
                                    catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Location failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        else if (v.getId() == R.id.nextBtn)
        {
            Intent details = new Intent(this, RideDetailsActivity.class);
            details.putExtra("MATKA", matka);
            details.putExtra("AIKA", aika);
            details.putExtra("ALKUOSOITE", strLahto);
            details.putExtra("LOPPUOSOITE", strLoppu);
            details.putExtra("ALKUCITY", strLahtoCity);
            details.putExtra("LOPPUCITY", strLoppuCity);

            startActivityForResult(details, INTENT_ID);
        }
        else if (v.getId() == R.id.etappiBtn & lukitus == 0)
        {
            etappiEditori.setVisibility(View.VISIBLE);
            etappiRemoveBtn.setVisibility(View.VISIBLE);
            lukitus = 1;

        }
        else if (v.getId() == R.id.etappiBtn & lukitus == 1)
        {
            etappiEditori2.setVisibility(View.VISIBLE);
            etappiRemoveBtn2.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.etappiRemoveBtn)
        {
            etappiEditori.setVisibility(View.GONE);
            etappiRemoveBtn.setVisibility(View.GONE);
            strWaypoint1 = "";
            etappiEditori.setQuery(strWaypoint1, true);
            lukitus = 0;
        }
        else if (v.getId() == R.id.etappiRemoveBtn2)
        {
            etappiEditori2.setVisibility(View.GONE);
            etappiRemoveBtn2.setVisibility(View.GONE);
            strWaypoint2 = "";
            etappiEditori2.setQuery(strWaypoint2, true);
        }

    }


    public void geoLocate(String start, String stop, String waypoint1, String waypoint2) throws IOException {
        mMap.clear();

        Geocoder gc = new Geocoder(this);
        double startLat = 0;
        double startLon = 0;
        double stopLat = 0;
        double stopLon = 0;

        try
        {
            List<Address> listStart = gc.getFromLocationName(start, 1);
            Address add = listStart.get(0);
            startLat = add.getLatitude();
            startLon = add.getLongitude();
            strLahtoCity = add.getLocality();
            Log.d("TESTIII", "startcity " + strLahtoCity);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Start position wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            List<Address> listStop = gc.getFromLocationName(stop, 1);
            Address add2 = listStop.get(0);
            stopLat = add2.getLatitude();
            stopLon = add2.getLongitude();
            strLoppuCity = add2.getLocality();
            Log.d("TESTIII", "endtcity " + strLoppuCity);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Destination wrong", Toast.LENGTH_SHORT).show();
            return;
        }


        if(strWaypoint1 != null && !strWaypoint1.isEmpty())
        {
            try {
                List<Address> listWay1 = gc.getFromLocationName(waypoint1,1);
                Address add3 = listWay1.get(0);
                double way1Lat = add3.getLatitude();
                double way1Lon = add3.getLongitude();
                wayPoint1 = new MarkerOptions().position(new LatLng(way1Lat, way1Lon)).title("WayPoint1");
                mMap.addMarker(wayPoint1);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Waypoint wrong", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        if(strWaypoint2 != null && !strWaypoint2.isEmpty())
        {
            try {
                List<Address> listWay2 = gc.getFromLocationName(waypoint2,1);
                Address add4 = listWay2.get(0);
                double way2Lat = add4.getLatitude();
                double way2Lon = add4.getLongitude();
                wayPoint2 = new MarkerOptions().position(new LatLng(way2Lat, way2Lon)).title("WayPoint2");
                mMap.addMarker(wayPoint2);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Waypoint wrong", Toast.LENGTH_SHORT).show();
            }

        }
        /*if(latitude != null && !latitude.isEmpty()){
            Double gpsLat = Double.valueOf(latitude);
            Double gpsLon = Double.valueOf(longitude);
            place1 = new MarkerOptions().position(new LatLng(gpsLat, gpsLon)).title("Location 1");
            latitude = null;
            longitude = null;
        }*/
        else {
            place1 = new MarkerOptions().position(new LatLng(startLat, startLon)).title("Location 1");
    }

        place2 = new MarkerOptions().position(new LatLng(stopLat, stopLon)).title("Location 2");


        animationHandler();
        new FetchURL(RouteActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        routeDetails.setVisibility(View.VISIBLE);

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

        if(wayPoint1 != null)
        {
            parameters = str_origin + "&" + str_dest + "&waypoints=via:" + wayPoint1.getPosition().latitude + "," + wayPoint1.getPosition().longitude + "&" + mode;
        }
        if(wayPoint1 != null & wayPoint2 != null)
        {
            parameters = str_origin + "&" + str_dest + "&waypoints=via:" + wayPoint1.getPosition().latitude + "," +
                    wayPoint1.getPosition().longitude + "|via:" + wayPoint2.getPosition().latitude + "," + wayPoint2.getPosition().longitude + "&" + mode;
        }
        if(wayPoint2 != null & wayPoint1 == null)
        {
            parameters = str_origin + "&" + str_dest + "&waypoints=via:" + wayPoint2.getPosition().latitude + "," + wayPoint2.getPosition().longitude + "&" + mode;
        }
        //String parameters = str_origin + "&" + str_dest + 64.080600,%2024.533221" + "&" + mode;
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

        TextView distance = (TextView) findViewById(R.id.infoTxt);
        TextView duration = (TextView) findViewById(R.id.infoTxt2);

        matka = Constant.DISTANCE;
        aika = Constant.DURATION;
        distance.setText(Constant.DISTANCE + " km ");
        duration.setText(Constant.DURATION);

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
            int range = newPart.range;

            /*TextView teksti = (TextView) findViewById(R.id.testailua);
            teksti.setText(date+ " " + time + " " + passenger + " " + price + " " + range);*/

        }
    }

}
