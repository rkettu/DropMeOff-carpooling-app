package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GetRideActivity extends AppCompatActivity {

    String tripListViewItemStartPoint, tripListViewItemEndPoint, tripListViewItemDateTime, tripListViewItemEstTime;
    String stringDate, stringEstTime;
    ArrayList<GetARideUtility> arrayList = new ArrayList<>();
    EditText startPointEditText, endPointEditText, dateEditText, estTimeEditText;
    ListView tripListView;
    private GetARideAdapter getARideAdapter;
    String TAG = "SWAG";
    private int newYear, newMonth, newDay, newHour, newMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride);

        startPointEditText = findViewById(R.id.startPointEditText);
        endPointEditText = findViewById(R.id.endPointEditText);
        dateEditText = findViewById(R.id.dateEditText);
        estTimeEditText = findViewById(R.id.estTimeEditText);

        tripListView = findViewById(R.id.tripsListView);
        getARideAdapter = new GetARideAdapter(this, R.layout.adapter_get_a_ride, arrayList);

        arrayList.add(new GetARideUtility("30.3.2020", "12:30", "Oulu", "Helsinki", "JiiKorni"));
        arrayList.add(new GetARideUtility("1.4.2020", "11:30", "Oulu", "Helsinki", "SJaMsa"));
        arrayList.add(new GetARideUtility("5.4.2020", "15:15", "Oulu", "Rovaniemi", "SMH"));
        arrayList.add(new GetARideUtility("3.4.2020", "16:00", "Oulu", "Kittilä", "JKampela"));
        arrayList.add(new GetARideUtility("27.3.2020", "18:00", "Tornio", "Keminmaa", "RooPeK"));
        arrayList.add(new GetARideUtility("15.4.2020", "19:00", "Vaasa", "Landia", "muita"));
        arrayList.add(new GetARideUtility("3.5.2020", "8:00", "Oulu", "Pyhäjärvi", "kayttajia"));

    }

    public void searchButton(View v) {
        hideKeyboard(this);
        tripListView.setAdapter(getARideAdapter);
        final String startPoint = startPointEditText.getText().toString();
        final String endPoint = endPointEditText.getText().toString();
        final String dateTime = dateEditText.getText().toString();
        final String estTime = estTimeEditText.getText().toString();

        for (int i = 0; i < arrayList.size(); i++) {
            tripListViewItemStartPoint = arrayList.get(i).getStartPoint();
            tripListViewItemEndPoint = arrayList.get(i).getEndPoint();
            tripListViewItemDateTime = arrayList.get(i).getTripDate();
            tripListViewItemEstTime = arrayList.get(i).getTripStartTime();
            

            //-----------Changing start point and end point to coordinates----------//
            try {
                geoLocate(startPoint, endPoint);
            } catch (IOException e) {
                e.printStackTrace();
            }

            tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //-------Trip Info-------//

                }
            });
        }
    }

    public void dateOfTimeClicked(View v){
        final Calendar calendar = Calendar.getInstance();
        newYear = calendar.get(Calendar.YEAR);
        newMonth = calendar.get(Calendar.MONTH);
        newDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                stringDate = (dayOfMonth + "-" + (month + 1) + "-" + year);
                dateEditText.setText(stringDate);
            }
        }, newYear, newMonth, newDay); dpd.show();
    }

    public void estTimeClicked(View v){
        final Calendar calendar = Calendar.getInstance();
        newHour = calendar.get(Calendar.HOUR_OF_DAY);
        newMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                stringEstTime = (hourOfDay + ":" + minute);
                estTimeEditText.setText(stringEstTime);
            }
        }, newHour, newMinute, true); tpd.show();
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
