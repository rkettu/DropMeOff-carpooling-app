package com.example.mobproj2020new;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BookedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView headline, name, phone, distance, price, dateOfTrip, duration, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_trips_info);
        findViewById(R.id.removeTrip).setOnClickListener(this);

        headline = findViewById(R.id.tripHeadline);
        name = findViewById(R.id.nameText);
        phone = findViewById(R.id.phoneNum);
        distance = findViewById(R.id.distanceNum);
        price = findViewById(R.id.costSum);
        dateOfTrip = findViewById(R.id.infoDate);
        duration = findViewById(R.id.durationTime);
        info = findViewById(R.id.infoBox);

        headline.setText("Oulu - Helsinki");
        name.setText("Urho Kekkonen");
        phone.setText("+3584621944131");
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

    }

    //------------------Varauksen poisto-----------------//
    public void removeTripBtn(View v){

    }
}