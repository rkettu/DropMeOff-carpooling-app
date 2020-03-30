package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class OfferedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView headline, distance, price, dateOfTrip,duration,info;
    ArrayList<OfferedTrips> tripInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_trips_info);
        findViewById(R.id.removeTrip).setOnClickListener(this);
        findViewById(R.id.editTrip).setOnClickListener(this);

        headline = findViewById(R.id.tripHeadline);
        distance = findViewById(R.id.distanceNum);
        price = findViewById(R.id.costSum);
        dateOfTrip = findViewById(R.id.infoDate);
        duration = findViewById(R.id.durationTime);
        info = findViewById(R.id.infoBox);

        //-------Shows up in all trips now-------//
        headline.setText("Oulu - Helsinki");
        distance.setText("600km");
        price.setText("18€");
        dateOfTrip.setText("15.7.2020 - klo 14:00");
        duration.setText("8h");
        info.setText("Only one luggage per passenger");

        }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.removeTrip){

        }
        if (v.getId() == R.id.editTrip){

        }

    }

    //--------------Button edit trip info-------------//
    public void editTripBtn(View v){                          //----Random activity, in the end where actual editing happens-----//
        Intent i = new Intent(OfferedTripsInfoActivity.this, MainActivity.class);
        startActivity(i);

    }

    //------------------Button remove trip-----------------//
    public void removeTripBtn(View v){

    }

    //------Not sure if even needed...-----------//
    public class OfferedTrips{

        private String headline;
        private float distance;
        private float price;
        private double dateOfTrip;
        private float duration;
        private String info;

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public double getDateOfTrip() {
            return dateOfTrip;
        }

        public void setDateOfTrip(double dateOfTrip) {
            this.dateOfTrip = dateOfTrip;
        }

        public float getDuration() {
            return duration;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
