package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pick_up_or_transportation);

        FirebaseAuth.AuthStateListener als = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    AppUser.init();
                    CheckProfileCreated();
                }
                else
                {
                    FirebaseHelper.loggedIn = false;
                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(als);
    }

    private void CheckProfileCreated()
    {
        DatabaseHandler db= new DatabaseHandler();
        db.checkProfileCreated(getApplicationContext());
    }

    //-----------Applications settings button------------//
    public void AppSettings(View v) {
        final String[] itemList = {"Settings", "My Profile", "About", "Sign Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(itemList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //Log.d("SWAG", "onClick: settings");
                        break;
                    case 1:
                        //Log.d("SWAG", "onClick: My Profile");
                        if(FirebaseHelper.loggedIn) {
                            DatabaseHandler db = new DatabaseHandler();
                            db.GoToProfile(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid());
                        } else {
                            FirebaseHelper.GoToLogin(getApplicationContext());
                        }
                        break;
                    case 2:
                        //Log.d("SWAG", "onClick: About");
                        break;
                    case 3:
                        //Log.d("SWAG", "onClick: Sign Out");
                        if(FirebaseHelper.loggedIn) {
                            AppUser.del();

                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        }
                }
            }
        });
        AlertDialog settingsDialog = builder.create();
        settingsDialog.show();
        settingsDialog.getWindow().setDimAmount(0);
        settingsDialog.getWindow().setLayout(420, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        settingsDialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
        settingsDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.settings_dialog_background));
    }

    //----------------Button BookedTrips----------------//
    public void SelectBookedTrips(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booked trips");

        //------------List to test AlertDialog---------//
        final List<String> trips = new ArrayList<>();
        trips.add("Oulu - Helsinki");
        trips.add("Oulu - Kannus");
        trips.add("Oulu - Pyhäjärvi");
        trips.add("Helsinki - Oulu");
        trips.add("Helsinki - Jyväskylä - Oulu");



        ArrayAdapter<String> bookedTrips = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, trips);
        builder.setAdapter(bookedTrips, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, trips.get(which) + " trip chosen ", Toast.LENGTH_LONG).show();

                ///////////////////Varattujen matkojen info näkymä\\\\\\\\\\\\\\\\\\\\
                Intent i = new Intent(MainActivity.this, BookedTripsInfoActivity.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //---------------Button OfferTrips---------------//
    public void SelectOfferedTrips(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Offered rides");


        //------------List to test AlertDialog---------//
        final List<String> rides = new ArrayList<>();
        rides.add("Oulu - Helsinki");
        rides.add("Oulu - Kannus");
        rides.add("Oulu - Pyhäjärvi");
        rides.add("Helsinki - Oulu");
        rides.add("Jyväskylä - Oulu");

        ArrayAdapter<String> offeredRides = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, rides);
        builder.setAdapter(offeredRides, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, rides.get(which) + " ride chosen ", Toast.LENGTH_LONG).show();

                /////////////////Offered rides view\\\\\\\\\\\\\\\\\\
                Intent i = new Intent(MainActivity.this, OfferedTripsInfoActivity.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //-------------Button Get A Ride----------------//
    public void SelectGetARide(View v){
        Intent GetARideIntent = new Intent(MainActivity.this, GetRideActivity.class);
        startActivity(GetARideIntent);
    }

    //------------Button Offer A Ride--------------//
    public void SelectOfferARide(View v){
        Intent intent = new Intent(MainActivity.this, RouteActivity.class);
        startActivity(intent);
    }

    //Exit app with pressing back putton on your phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}