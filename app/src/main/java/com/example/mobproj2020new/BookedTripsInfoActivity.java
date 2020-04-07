package com.example.mobproj2020new;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookedTripsInfoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView startPointTextView, destinationTextView, nameTextView, phoneTextView, distanceTextView,
             priceTextView, dateOfTripTextView, durationTextView, infoTextView;

    GetARideAdapter getARideAdapter;

    private String bStartP, bDest, bName, bPhone, bDistance, bPrice, bDateOfTrip, bDuration, bInfo, bDate, bRideId;
    private ArrayList<GetARideUtility> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_trips_info);
        findViewById(R.id.removeTrip).setOnClickListener(this);

        getARideAdapter = new GetARideAdapter(this, GetARideUtility.arrayList);

        startPointTextView = findViewById(R.id.tripStartPoint);
        destinationTextView = findViewById(R.id.tripDestination);
        nameTextView = findViewById(R.id.nameText);
        phoneTextView = findViewById(R.id.phoneNum);
        distanceTextView = findViewById(R.id.distanceNum);
        priceTextView = findViewById(R.id.costSum);
        dateOfTripTextView = findViewById(R.id.infoDate);
        durationTextView = findViewById(R.id.durationTime);
        infoTextView = findViewById(R.id.infoBox);

               //------------Makes app crash---------------//
        Bundle bundle = getIntent().getExtras();
        bStartP = bundle.getString("start");
        bDest = bundle.getString("destination");
        bName = bundle.getString("name");
        bPhone = bundle.getString("phone");
        bDistance = bundle.getString("distance");
        bDate = bundle.getString("date");
        bDuration = bundle.getString("duration");
        bPrice = bundle.getString("price");
        bInfo = bundle.getString("info");
        bRideId = bundle.getString("rideId");


        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(Long.parseLong(bDate));
        String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+
                                " - "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);

        startPointTextView.setText(bStartP);
        destinationTextView.setText(bDest);
        nameTextView.setText(bName);
        //dateOfTripTextView.setText(timeString);
        durationTextView.setText(bDuration);
        priceTextView.setText(bPrice + "per Kilometer");

        /*          /---Hard coded info---//
        startPointTextView.setText("Oulu" + "\r-");
        destinationTextView.setText("Helsinki");
        nameTextView.setText("Urho Kekkonen");
        phoneTextView.setText("+3584621944131");
        distanceTextView.setText("600km");
        priceTextView.setText("18€");
        dateOfTripTextView.setText("15.7.2020 - klo 14:00");
        durationTextView.setText("8h");
        infoTextView.setText("Only one luggage per passenger");*/

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.removeTrip){

        }

    }

    public void btnBackArrow(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    //------------------Varauksen poisto-----------------//
    public void removeTripBtn(View v){
        if(FirebaseHelper.loggedIn){
            //removeTripDialog();
        } else{
            Toast.makeText(getApplicationContext(), "Please log in or sign up to book this trip.",Toast.LENGTH_LONG).show();
            FirebaseHelper.GoToLogin(getApplicationContext());
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