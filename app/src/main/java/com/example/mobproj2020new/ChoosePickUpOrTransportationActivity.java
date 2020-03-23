package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;

public class ChoosePickUpOrTransportationActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pick_up_or_transportation);
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
                        break;
                    case 2:
                        //Log.d("SWAG", "onClick: About");
                        break;
                    case 3:
                        //Log.d("SWAG", "onClick: Sign Out");
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

    //----------------Button BookedTrips----------------//
    public void SelectBookedTrips(View v){

    }

    //---------------Button OfferTrips---------------//
    public void SelectOfferedTrips(View v){

    }

    //-------------Button Get A Ride----------------//
    public void SelectGetARide(View v){
        Intent GetARideIntent = new Intent(ChoosePickUpOrTransportationActivity.this, GetRideActivity.class);
        startActivity(GetARideIntent);
    }

    //------------Button Offer A Ride--------------//
    public void SelectOfferARide(View v){
        Intent intent = new Intent(ChoosePickUpOrTransportationActivity.this, RouteActivity.class);
        startActivity(intent);
    }
}
