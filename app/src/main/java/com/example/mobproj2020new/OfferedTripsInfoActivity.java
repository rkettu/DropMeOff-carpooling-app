package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class OfferedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private float distance;
    private float price;
    private String dateOfTrip;
    private float duration;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_trips_info);
        }

    @Override
    public void onClick(View v) {

    }

    //--------------Matkan varauksen muokkaus-------------//
    public void editTripBtn(View v){

    }

    //------------------Varauksen poisto-----------------//
    public void removeTripBtn(View v){

    }
}
