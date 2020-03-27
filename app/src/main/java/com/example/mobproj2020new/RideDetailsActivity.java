package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RideDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private Calendar mC;
    private String strDate;
    private String strTime;
    private int passengers = 1;
    private float hinta;
    private int range;

    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int pickedYear, pickedMonth, pickedDate, pickedHour, pickedMinute;
    SeekBar seekBar;
    SeekBar seekBar2;
    TextView hintaTxt, exampleTxt, minRange;
    private Button confirmBtn;
    String newMatka;
    String newAika;
    String newLahtoOs;
    String newLoppuOs;
    Double doubleMatka;
    int intMatka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        mC = Calendar.getInstance();

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                newMatka = null;
            }else{
                newMatka = extras.getString("MATKA");
                newAika = extras.getString("AIKA");
                newLahtoOs = extras.getString("ALKUOSOITE");
                newLoppuOs = extras.getString("LOPPUOSOITE");
        }
        }
        else {
            newMatka = (String)savedInstanceState.getSerializable("MATKA");
            newAika = (String)savedInstanceState.getSerializable("AIKA");
            newLahtoOs = (String)savedInstanceState.getSerializable("ALKUOSOITE");
            newLoppuOs = (String)savedInstanceState.getSerializable("LOPPUOSOITE");
        }

        if(newMatka != null && !newMatka.isEmpty()){
            doubleMatka = Double.valueOf(newMatka);
            intMatka = Integer.valueOf(doubleMatka.intValue());
        }
        NumberPicker np = findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(10);

        final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        confirmBtn.setEnabled(false);


        txtDate=(EditText)findViewById(R.id.dateEdit);
        txtTime=(EditText)findViewById(R.id.timeEdit);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        hintaTxt = (TextView)findViewById(R.id.seekBarText);
        exampleTxt = (TextView)findViewById(R.id.examplePrice);
        minRange = (TextView) findViewById(R.id.minimumRangeTxt);
        minRange.setText("Min range: " + newMatka);

        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar2=(SeekBar)findViewById(R.id.rangeValue);
        seekBar2.setProgress(100);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                passengers = newVal;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hinta = ((float) progress / 1000);
                hintaTxt.setText(String.format("%.3f", hinta) + " per kilometer");
                exampleTxt.setText("Example km: " + newMatka + " km \n" + "Price: " + String.format("%.2f", doubleMatka * hinta) + " eur") ;
                if(strTime != null & strDate!= null & hinta > 0 ){
                    confirmBtn.setEnabled(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                range = ((intMatka / 100) * progress);
                minRange.setText("Min range: " + range);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == txtDate){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            strDate = (dayOfMonth + "-" + (month + 1) + "-" + year);
                            txtDate.setText(strDate);
                            pickedYear = year;
                            pickedMonth = month;
                            pickedDate = dayOfMonth;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if (v == txtTime){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            strTime = (hourOfDay + ":" + minute);
                            txtTime.setText(strTime);
                            pickedHour = hourOfDay;
                            pickedMinute = minute;
                        }
                    }, mHour, mMinute, true);

            timePickerDialog.show();
        }
        else if (v.getId() == R.id.confirmBtn)
        {
            /*
            RideDetailPart part = new RideDetailPart(strDate, strTime, passengers, hinta, range);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("DETAILS", part);
            setResult(RideDetailsActivity.RESULT_OK, resultIntent);
            finish();*/
            DatabaseHandler db = new DatabaseHandler();

            mC.set(pickedYear, pickedMonth, pickedDate, pickedHour, pickedMinute);
            long leaveTime = mC.getTimeInMillis();

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String duration = newAika;
            //String startDate = strDate;
            //String startTime = strTime;
            String startAddress = newLahtoOs;
            String endAddress = newLoppuOs;
            int freeSlots = passengers;
            float price = hinta;
            List<HashMap<String,String>> points = Constant.pointsList;
            List<String> waypointAddresses = Constant.waypointAddressesList;
            Route route = new Route(uid, duration, leaveTime, startAddress, endAddress,
                                    freeSlots, price, points, waypointAddresses);
            db.createRide(route, RideDetailsActivity.this);
        }
    }

}
