package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    private static CircleImageView btnSettings;
    private static String image;
    private static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pick_up_or_transportation);

        Log.d("TAG", "onCreate: ");
        btnSettings = findViewById(R.id.btnSettings);

        FirebaseAuth.AuthStateListener als = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Log.d("TAG", "onAuthStateChanged: true");
                    AppUser.init();
                    CheckProfileCreated();
                }
                else
                {
                    Log.d("TAG", "onAuthStateChanged: false");
                    FirebaseHelper.loggedIn = false;
                    setImageSettings(MainActivity.this);
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

    public static void setImageSettings(final Context context) {
        Log.d("TAG", "mennään = setImageSettings: ");
        if (FirebaseHelper.loggedIn) {
            uid = FirebaseAuth.getInstance().getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profpics/" + uid);
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.getResult() != null) {
                        image = String.valueOf(task.getResult());
                        Log.d("TAG", "onComplete: " + image);
                        Picasso.with(context).load(image).into(btnSettings);
                    }

                }
            });
        }
        else{
                Log.d("TAG", "setImageSettings: settingnappia laitetaan");btnSettings.setImageResource(R.drawable.ic_settings_icon_foreground);
        }
    }

    //-----------Applications settings button------------//
    final String[] itemListLoggedOut = {"Log In"};
    final String[] itemListLoggedIn = {"Settings", "My Profile", "About", "Sign Out"};
    public void AppSettings(View v) {
        if(FirebaseHelper.loggedIn){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(itemListLoggedIn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            Log.d("SWAG", "onClick: settings");
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
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setItems(itemListLoggedOut, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            Log.d("SWAG", "onClick: ");
                            FirebaseHelper.GoToLogin(getApplicationContext());
                            break;
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
    }

    //----------------Button BookedTrips----------------//
    public void SelectBookedTrips(View v){
        SleepReceiver.setAlarm(getApplicationContext(), Calendar.getInstance().getTimeInMillis() + 10000, "HEI KYYTIS LÄHTEE", "Että sellaista asiaa oli...");
        /*
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
        dialog.show();*/
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