package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetRideActivity extends AppCompatActivity{

    String tripListViewItemStartPoint;
    String tripListViewItemEndPoint;
    ArrayList<GetARideUtility> arrayList = new ArrayList<>();
    CheckBox luggageCheckBox;
    EditText luggageEditText;
    EditText startPointEditText;
    EditText endPointEditText;
    ListView tripListView;
    private GetARideAdapter getARideAdapter;
    String TAG = "SWAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride);

        tripListView = findViewById(R.id.tripsListView);
        getARideAdapter = new GetARideAdapter(this, R.layout.adapter_get_a_ride, arrayList);
        tripListView.setAdapter(getARideAdapter);

        arrayList.removeAll(arrayList);
        arrayList.add(new GetARideUtility("10km", "9h","Oulu","Helsinki","JiiKorni"));
        arrayList.add(new GetARideUtility("15km", "24h","Jyväskylä","Lahti","SJaMsa"));
        arrayList.add(new GetARideUtility("20km", "10h","Ivalo","Rovaniemi","SMH"));
        arrayList.add(new GetARideUtility("30km", "60h","Sodankylä","Kittilä","JKampela"));
        arrayList.add(new GetARideUtility("35km", "6h 30min","Tornio","Keminmaa","RooPeK"));
        arrayList.add(new GetARideUtility("7km", "6h","Vaasa","Landia","muita"));
        arrayList.add(new GetARideUtility("342km", "8h","Oulu","Pyhäjärvi","kayttajia"));

        luggageCheckBox = findViewById(R.id.luggageCheckBox);
        luggageEditText = findViewById(R.id.luggageEditText);
        luggageEditText.setVisibility(View.INVISIBLE);

        luggageCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (luggageCheckBox.isChecked()) { luggageEditText.setVisibility(View.VISIBLE); }
                else { luggageEditText.setVisibility(View.INVISIBLE); }
            }
        });
    }

    public void searchButton(View v){
        hideKeyboard(this);
        startPointEditText = findViewById(R.id.startPointEditText);
        endPointEditText = findViewById(R.id.endPointEditText);
        final String startPoint = startPointEditText.getText().toString();
        final String endPoint = endPointEditText.getText().toString();

        for(int i = 0; i < arrayList.size(); i++){
            tripListViewItemStartPoint = arrayList.get(i).getStartPoint();
            tripListViewItemEndPoint = arrayList.get(i).getEndPoint();
            if(startPoint.equals(tripListViewItemStartPoint)) {
                if (endPoint.equals(tripListViewItemEndPoint)) {
                    tripListView.
                }
            }
        }

        //-----------Changing start point and end point to coordinates----------//
        try {
            geoLocate(startPoint, endPoint);
        } catch (IOException e) { e.printStackTrace(); }

        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //-------Trip Info-------//

            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) { v = new View(activity); }
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void geoLocate(String startPoint, String endPoint) throws IOException {
        Geocoder gc = new Geocoder(this);

        List<Address> listStart = gc.getFromLocationName(startPoint, 1);
        Address add = listStart.get(0);

        List<Address> listStop = gc.getFromLocationName(endPoint, 1);
        Address add2 = listStop.get(0);

        double startLat = add.getLatitude();
        double startLon = add.getLongitude();

        double stopLat = add2.getLatitude();
        double stopLon = add2.getLongitude();

        Log.d(TAG, "geoLocate: " + startLat + " " + startLon + " " + stopLat + " " + stopLon);
    }
}
