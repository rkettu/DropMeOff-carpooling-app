package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GetRideActivity extends AppCompatActivity {

    private int pickedYear1, pickedMonth1, pickedDate1;
    private int pickedHour1, pickedMinute1;
    private int pickedYear2, pickedMonth2, pickedDate2;
    private int pickedHour2, pickedMinute2;
    private Context context = this;
    String stringDate, stringEstTime;
    ArrayList<GetARideUtility> arrayList = new ArrayList<>();
    ArrayList<String> dialogList = new ArrayList<>();
    EditText startPointEditText, endPointEditText, dateEditText, estTimeEditText, dateEditText2, estTimeEditText2;
    ListView tripListView;
    GetARideAdapter getARideAdapter;
    int newYear, newMonth, newDay;
    int newHour, newMinute;
    private static final String TAG = "GetARideActivityTAG";
    Calendar mCalendar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride);
        startPointEditText = findViewById(R.id.startPointEditText);
        endPointEditText = findViewById(R.id.endPointEditText);
        dateEditText = findViewById(R.id.dateEditText);
        dateEditText2 = findViewById(R.id.dateEditText2);
        estTimeEditText = findViewById(R.id.estTimeEditText);
        estTimeEditText2 = findViewById(R.id.estTimeEditText2);
        tripListView = findViewById(R.id.tripsListView);
        textView = findViewById(R.id.tripsTextView);
        textView.setText("Find your ride here!");
        textView.setVisibility(View.VISIBLE);

        mCalendar = Calendar.getInstance();
        String currentDate = mCalendar.get(Calendar.DATE) + "." + (mCalendar.get(Calendar.MONTH)+1) + "." + mCalendar.get(Calendar.YEAR);
        int currentHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = mCalendar.get(Calendar.MINUTE);
        String format = "%1$02d";
        String estHour = String.format(format, currentHour);
        String estMin = String.format(format, currentMinute);
        dateEditText.setText(currentDate);
        estTimeEditText.setText(estHour + ":" + estMin);
    }

    private void dummyData(){
        String [] sp = new String [] {"Oulu", "Pyhäjärvi", "Helsinki", "Oulu", "Oulu", "Tornio", "Rovaniemi", "Kuopio"};
        String [] ep = new String [] {"Helsinki", "Oulu", "Oulu", "Helsinki", "Pyhäjärvi", "Keminmaa", "Tornio", "Kajaani"};
        String [] tp = new String[] {"1.4.2020", "3.4.2020", "5.4.2020", "29.3.2020", "30.3.2020", "30.3.2020", "1.4.2020", "2.4.2020"};
        String [] tst = new String[] {"9:30", "10:30", "11:15", "10:30", "9:30", "8:00", "19:00", "1:00"};
        String [] tu = new String[] {"Jiikorni", "SJMS", "JKampela", "RooPK", "SMH", "jami", "muita", "kayttajia"};
        for(int i = 0; i < sp.length; i++){
            //GetARideUtility utility = new GetARideUtility(sp[i], ep[i], tp[i], tst[i], tu[i]);
            //arrayList.add(utility);
        }
    }

    public static ProgressDialog pd;

    public static void shutPd(){
        pd.dismiss();
    }

    public void searchButton (View v){
        hideKeyboard(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading matching rides");
        pd.setCancelable(false);
        pd.show();

        textView.setVisibility(View.GONE);
        arrayList.removeAll(arrayList);

        String startPoint = startPointEditText.getText().toString();
        String endPoint = endPointEditText.getText().toString();

        try {
            geoLocate(startPoint, endPoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------date time calendar----------//

    public void dateOfTimeClicked(final View v){
        final Calendar calendar = Calendar.getInstance();
        newYear = calendar.get(Calendar.YEAR);
        newMonth = calendar.get(Calendar.MONTH);
        newDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                stringDate = (dayOfMonth + "." + (month + 1) + "." + year);
                pickedDate1 = newDay; pickedMonth1 = newMonth; pickedYear1 = newYear;
                if(v.getId() == R.id.dateEditText) {
                    pickedDate1 = dayOfMonth; pickedMonth1 = month; pickedYear1 = year;
                    dateEditText.setText(stringDate);
                }
                else if(v.getId() == R.id.dateEditText2) {
                    pickedDate2 = dayOfMonth; pickedMonth2 = month; pickedYear2 = year;
                    dateEditText2.setText(stringDate);
                }
            }
        }, newYear, newMonth, newDay); dpd.show();
    }

    //----------time calendar --------//
    public void estTimeClicked(final View v){
        final Calendar calendar = Calendar.getInstance();
        newHour = calendar.get(Calendar.HOUR_OF_DAY);
        newMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pickedHour1 = newHour; pickedMinute1 = newMinute;
                if(v.getId() == R.id.estTimeEditText) {
                    pickedHour1 = hourOfDay; pickedMinute1 = minute;
                    String format = "%1$02d";
                    String estHour = String.format(format, hourOfDay);
                    String estMin = String.format(format, minute);
                    stringEstTime = estHour + ":" + estMin;
                    estTimeEditText.setText(stringEstTime);
                }
                else if(v.getId() == R.id.estTimeEditText2) {
                    pickedHour2 = hourOfDay; pickedMinute2 = minute;
                    String format = "%1$02d";
                    String estHour = String.format(format, hourOfDay);
                    String estMin = String.format(format, minute);
                    stringEstTime = estHour + ":" + estMin;
                    estTimeEditText2.setText(stringEstTime);
                }
            }
        }, newHour, newMinute, true);
        tpd.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v == null) { v = new View(activity); }
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void geoLocate(final String startPoint, final String endPoint) throws IOException{

        textView.setText("Find your trip here!");
        Geocoder gc = new Geocoder(this);
        try{
            List<Address> listStart = gc.getFromLocationName(startPoint, 1);
            Address add = listStart.get(0);

            List<Address> listStop = gc.getFromLocationName(endPoint, 1);
            Address add2 = listStop.get(0);

            float startLat = (float) add.getLatitude();
            float startLon = (float) add.getLongitude();
            float stopLat = (float) add2.getLatitude();
            float stopLon = (float) add2.getLongitude();

            Log.d(TAG, "geoLocate: "+startLat+startLon+stopLat+stopLon);
            Log.d(TAG, "geoLocate: "+pickedHour1+pickedMinute1);
            Log.d(TAG, "geoLocate: "+pickedYear1+pickedMonth1+pickedDate1);
            // TODO: !!!! Require both time fields for search, maybe preset them to current day - week from current day
            mCalendar.set(pickedYear1, pickedMonth1, pickedDate1, pickedHour1, pickedMinute1);
            float t1 = mCalendar.getTimeInMillis();
            mCalendar.set(pickedYear2, pickedMonth2, pickedDate2, pickedHour2, pickedMinute2);
            float t2 = mCalendar.getTimeInMillis();

            final GetARideAdapter adapter = new GetARideAdapter(context, arrayList);

            final FindTripAsyncTask findTripASyncTask = new FindTripAsyncTask(new ReporterInterface() {

                @Override
                public void dialogData(final List<String> list) {

                    Log.d(TAG, "dialogData:" + list);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!list.contains("completed")) {
                                pd.dismiss();
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Cannot find trips");
                                list.clear();
                            } else {
                                textView.setVisibility(View.GONE);
                                list.clear();
                            }
                        }
                    });
                }


                @Override
                public void getTripData(final String uid, final String startAddress, final String endAddress, final String duration, final String rideId, String price, String leaveTime,
                                        final long freeSlots, final List<String> wayPoints, final List<String> participants, final String startCity, final String endCity) {

                    textView.setVisibility(View.GONE);
                    final float mPrice = Float.parseFloat(price);
                    final long mLeaveTime = Long.parseLong(leaveTime);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GetARideUtility utility = new GetARideUtility(uid,startAddress, endAddress, duration, rideId, mPrice, mLeaveTime, freeSlots,
                                    wayPoints, participants, startCity, endCity);

                            adapter.setUserStartPoint(startPoint);
                            adapter.setUserEndPoint(endPoint);
                            arrayList.add(utility);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }, GetRideActivity.this);
            findTripASyncTask.execute(startLat, startLon, stopLat, stopLon, t1, t2);
            tripListView.setAdapter(adapter);

        }
        catch (Exception e){
            e.printStackTrace();
            textView.setVisibility(View.VISIBLE);
            pd.dismiss();
            Toast.makeText(GetRideActivity.this, "Failed to find trips", Toast.LENGTH_SHORT).show();
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
}



