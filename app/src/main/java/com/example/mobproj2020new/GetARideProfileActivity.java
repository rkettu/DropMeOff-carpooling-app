package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetARideProfileActivity extends AppCompatActivity {

    TextView userNameTextView, startPointTextView, destinationTextView, startTimeTextView, durationTextView,
                priceTextView, freeSeatsTextView, waypointsTextView;
    CheckBox luggageCheckBox;
    EditText luggageEditText;
    CircleImageView progImageView;
    private String bUser, bUserPic, bStartP, bDest, bDate, bDur, bPrice, bSeats;
    private ArrayList<GetARideUtility> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_a_ride_profile);

        progImageView = findViewById(R.id.getRideImage);
        userNameTextView = findViewById(R.id.profTV1);
        startPointTextView = findViewById(R.id.profTV2);
        destinationTextView = findViewById(R.id.profTV3);
        startTimeTextView = findViewById(R.id.profTV4);
        durationTextView = findViewById(R.id.profTV5);
        priceTextView = findViewById(R.id.profTV6);
        freeSeatsTextView = findViewById(R.id.profTV7);
        waypointsTextView = findViewById(R.id.tv8);
        luggageEditText = findViewById(R.id.luggageET);
        luggageCheckBox = findViewById(R.id.luggageCB);

        luggageEditText.setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        bUserPic = bundle.getString("userPic");
        bUser = bundle.getString("user");
        bStartP = bundle.getString("start");
        bDest = bundle.getString("destination");
        bDate = bundle.getString("date");
        bDur = bundle.getString("duration");
        bPrice = bundle.getString("price");
        bSeats = bundle.getString("seats");

        Picasso.with(GetARideProfileActivity.this).load(bUserPic).into(progImageView);
        userNameTextView.setText("Ride provider: " + bUser);
        startPointTextView.setText("Start point: " + bStartP);
        destinationTextView.setText("Destination: " + bDest);
        startTimeTextView.setText("Start date: " + bDate);
        durationTextView.setText("Duration: " + bDur);
        priceTextView.setText("Price: " + bPrice);
        freeSeatsTextView.setText("Available seats: " + bSeats);
    }

    public void cbOnClick(View view){
        if(luggageCheckBox.isChecked()){
            luggageEditText.setVisibility(View.VISIBLE);
        }
        else {
            luggageEditText.setVisibility(View.INVISIBLE);
        }
    }

    public void btnBookTrip(View view){
        //-------- Book your trip -------/
        if(FirebaseHelper.loggedIn){
            //------if you are logged in------/

        } else{
            Toast.makeText(getApplicationContext(), "Please log in or sign up to book this trip.",Toast.LENGTH_LONG).show();
            FirebaseHelper.GoToLogin(getApplicationContext());
        }
    }

    public void goToProfile(View view){
        //-------- Go to ride provider profile ---------//
    }
}
