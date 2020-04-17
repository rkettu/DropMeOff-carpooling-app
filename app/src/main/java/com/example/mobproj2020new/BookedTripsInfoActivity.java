package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView headingTV,leaveTimeTV,durationTV,distanceTV,priceTV,driverHeadingTV,driverNameTV,driverPhoneTV;
    CircleImageView driverProfileImg;

    User mUser = null;
    Route mRoute = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_trips_info);

        findViewById(R.id.bookedCancelBookingButton).setOnClickListener(this);

        mRoute = (Route)getIntent().getSerializableExtra("MYKEY1");
        mUser = (User)getIntent().getSerializableExtra("MYKEY2");


        headingTV = findViewById(R.id.bookedHeading);
        leaveTimeTV = findViewById(R.id.bookedLeaveTime);
        durationTV = findViewById(R.id.bookedDuration);
        distanceTV = findViewById(R.id.bookedDistance);
        priceTV = findViewById(R.id.bookedPrice);
        driverHeadingTV = findViewById(R.id.bookedDriverHeading);
        driverNameTV = findViewById(R.id.bookedDriverName);
        driverPhoneTV = findViewById(R.id.bookedDriverPhone);
        driverProfileImg = findViewById(R.id.bookedDriverImage);

        DecimalFormat df = new DecimalFormat("#.##");

        if(mRoute != null)
        {
            headingTV.setText(mRoute.getStartAddress() + " - " + mRoute.getEndAddress());
            leaveTimeTV.setText(CalendarHelper.getFullTimeString(mRoute.getLeaveTime()));
            durationTV.setText(mRoute.getDuration());
            distanceTV.setText(mRoute.getDistance() + " km");
            priceTV.setText(df.format((double)mRoute.getPrice() * (double)mRoute.getDistance()) + " €");
        }
        if(mUser != null)
        {
            driverNameTV.setText(mUser.getFname());
            driverPhoneTV.setText(mUser.getPhone());
            Picasso.with(BookedTripsInfoActivity.this).load(mUser.getImgUri()).into(driverProfileImg);
            driverProfileImg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    new DatabaseHandler().GoToProfile(getApplicationContext(), mUser.getUid());
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bookedCancelBookingButton){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to remove yourself from this trip?");
            builder.setMessage("Repeat offenders may be penalized according to the Terms of Service.");
            builder.setCancelable(true);
            builder.setPositiveButton("CANCEL BOOKING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("CANCELLING", "canceling trip... " + which);
                    if(mRoute.getParticipants().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        // TODO: delete user from trip's participants, add one extra slot back to trip's freeSlots
                        Intent i = new Intent(BookedTripsInfoActivity.this, MainActivity.class);
                        Toast.makeText(BookedTripsInfoActivity.this, "Booking deleted successfully", Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(BookedTripsInfoActivity.this, "You're not even on this trip, mate", Toast.LENGTH_LONG).show();
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

    /*
    private void removeTripDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove booked trip?");
        builder.setCancelable(true);
        builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler db = new DatabaseHandler();
                db.RemoveTrip(bRideId, FirebaseAuth.getInstance().getCurrentUser().getUid());

            }
        });
    }*/

}