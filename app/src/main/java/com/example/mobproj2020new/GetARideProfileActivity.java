package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.contentcapture.DataRemovalRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetARideProfileActivity extends AppCompatActivity {

    TextView userNameTextView, startPointTextView, destinationTextView, startTimeTextView, durationTextView,
                priceTextView, freeSeatsTextView, waypointsTextView;
    CheckBox luggageCheckBox;
    EditText luggageEditText;
    CircleImageView progImageView;
    private String bUser, bUserPic, bStartP, bDest, bDate, bDur, bPrice, bSeats, bRideId;
    private ArrayList<GetARideUtility> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_a_ride_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
        bRideId = bundle.getString("rideId");

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(Long.parseLong(bDate));
        String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+" - "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);

        Picasso.with(GetARideProfileActivity.this).load(bUserPic).into(progImageView);
        userNameTextView.setText("Ride provider: " + bUser);
        startPointTextView.setText("Start point: " + bStartP);
        destinationTextView.setText("Destination: " + bDest);
        startTimeTextView.setText("Leaves at: " + timeString);
        durationTextView.setText("Duration: " + bDur);
        priceTextView.setText("Price: " + bPrice + "per Kilometer");
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
            BookTripDialog();
        } else{
            Toast.makeText(getApplicationContext(), "Please log in or sign up to book this trip.",Toast.LENGTH_LONG).show();
            FirebaseHelper.GoToLogin(getApplicationContext());
        }
    }

    public void goToProfile(View view){
        //-------- Go to ride provider profile ---------//
    }

    private void BookTripDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Booking this trip?");
        builder.setMessage("The estimated cost for this trip is " + bPrice + " per Kilometer");
        builder.setCancelable(true);
        builder.setPositiveButton("BOOK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler db = new DatabaseHandler();
                db.BookTrip(getApplicationContext(), bRideId,FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
