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

import com.google.firebase.auth.FirebaseAuth;

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
        DatabaseHandler db = new DatabaseHandler();
        db.GoToProfile(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    //---------------Button OfferTrips---------------//
    public void SelectOfferedTrips(View v){
        DatabaseHandler db = new DatabaseHandler();
        db.getMatchingRoutes(30f, 64.99960786f, 25.50759315f, 62.27942323f, 25.7258606f);
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
}
