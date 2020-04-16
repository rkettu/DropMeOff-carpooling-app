package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OfferedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView headline, dateOfTrip, duration, participants, pricePer, priceTotal;
    ArrayList<OfferedTrips> tripInfo = new ArrayList<>();
    Route thisRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_trips_info);

        findViewById(R.id.cancelTripButton).setOnClickListener(this);

        headline = findViewById(R.id.headingTV);
        dateOfTrip = findViewById(R.id.leaveTimeTV);
        duration = findViewById(R.id.durationTV);
        participants = findViewById(R.id.participantsAmountTV);
        pricePer = findViewById(R.id.pricePerTV);
        priceTotal = findViewById(R.id.priceTotalTV);

        DecimalFormat df = new DecimalFormat("#.##");

        thisRoute = (Route) getIntent().getSerializableExtra("MYKEY");
        if(thisRoute != null)
        {
            int participantsAmount;
            try {
                participantsAmount = thisRoute.getParticipants().size();
            } catch(Exception e) {
                participantsAmount = 0;
            }

            headline.setText(thisRoute.getStartAddress() + " - " + thisRoute.getEndAddress());
            dateOfTrip.setText(CalendarHelper.getFullTimeString(thisRoute.getLeaveTime()));
            duration.setText(thisRoute.getDuration());
            participants.setText(participantsAmount + " / " + (participantsAmount + thisRoute.getFreeSlots()));
            pricePer.setText(df.format((double)thisRoute.getPrice() * (double)thisRoute.getDistance()) + " €");
            priceTotal.setText(df.format((double)thisRoute.getPrice() * (double)thisRoute.getDistance()*participantsAmount*0.8) + " €"); // 0.8 because we take 20%

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelTripButton){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to cancel this trip?");
            builder.setMessage("Repeat offenders may be penalized according to the Terms of Service.");
            builder.setCancelable(true);
            builder.setPositiveButton("DELETE TRIP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("CANCELLING", "canceling trip... " + which);
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(thisRoute.getUid()))
                    {
                        // TODO: delete document from firestore and send notification to participants (Firebase Cloud Messaging, users subscribe to topic "*ride id here*" on booking, publish cancel message to topic here)
                        Intent i = new Intent(OfferedTripsInfoActivity.this, MainActivity.class);
                        Toast.makeText(OfferedTripsInfoActivity.this, "Trip cancelled successfully", Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(OfferedTripsInfoActivity.this, "Not your trip to cancel, mate", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("CANCELLING", "not canceling trip" + which);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
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
