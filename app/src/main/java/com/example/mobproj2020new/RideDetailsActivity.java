package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class RideDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Calendar mC;
    private String strDate;
    private String strTime;
    private int passengers = 4;
    private float hinta;
    private int range;

    EditText txtDate, txtTime, np;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int pickedYear, pickedMonth, pickedDate, pickedHour, pickedMinute;
    //seek1 hinta, seek2 pickupRange, seek3 range
    SeekBar seekBar, seekBar2, seekBar3;
    TextView hintaTxt, exampleTxt, minRange, rangeValueTextView;
    TextView pass;
    EditText pickUpDistanceEditText;
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

        mC = new GregorianCalendar();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newMatka = null;
            } else {
                newMatka = extras.getString("MATKA");
                newAika = extras.getString("AIKA");
                newLahtoOs = extras.getString("ALKUOSOITE");
                newLoppuOs = extras.getString("LOPPUOSOITE");
            }
        } else {
            newMatka = (String) savedInstanceState.getSerializable("MATKA");
            newAika = (String) savedInstanceState.getSerializable("AIKA");
            newLahtoOs = (String) savedInstanceState.getSerializable("ALKUOSOITE");
            newLoppuOs = (String) savedInstanceState.getSerializable("LOPPUOSOITE");
        }

        if (newMatka != null && !newMatka.isEmpty()) {
            doubleMatka = Double.valueOf(newMatka);
            intMatka = Integer.valueOf(doubleMatka.intValue());
        }
        np = findViewById(R.id.numberPicker);
        //np.setMinValue(1);
        //np.setMaxValue(10);

        final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(this);
        confirmBtn.setEnabled(false);


        txtDate = (EditText) findViewById(R.id.dateEdit);
        txtTime = (EditText) findViewById(R.id.timeEdit);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        hintaTxt = (TextView) findViewById(R.id.seekBarPriceText);
        exampleTxt = (TextView) findViewById(R.id.examplePrice);
        minRange = (TextView) findViewById(R.id.pickUpRangeTxt);
        minRange.setText("PickUp range: " + 5 + "km");
        rangeValueTextView = (TextView) findViewById(R.id.rangeValueTextView);
        rangeValueTextView.setText("Minimum Passanger trip: " + intMatka + "km");

        seekBar = (SeekBar) findViewById(R.id.priceSeekBar);
        seekBar2 = (SeekBar) findViewById(R.id.rangeValue);
        seekBar2.setProgress(100);
        seekBar3 = (SeekBar) findViewById(R.id.pickUpRangeValue);
        seekBar3.setProgress((int) ((5.00f / intMatka) * 100.00f));

        Log.d("####matka1####", intMatka + ", " + (int) (intMatka * 0.05f) + ", " + (intMatka / 100.00f));

        Log.d("####matka3####", intMatka + ", " + (intMatka / 100.00f) + ", " + ((5.00f / intMatka) * 100.00f) + ", " + intMatka * (5.00f / intMatka));

        exampleTxt.setText("Example km: " + newMatka + " km \n" + "Price: 0.00 eur");

        np = findViewById(R.id.numberPicker);
        //passengers = Integer.parseInt(np.getText().toString());
        /*np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                passengers = newVal;
            }
        });*/

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hinta = ((float) progress / 1000);
                hintaTxt.setText(String.format("%.3f", hinta) + " per kilometer");
                hintaTxt.setTextColor(Color.WHITE);
                exampleTxt.setText("Example km: " + newMatka + " km \n" + "Price: " + String.format("%.2f", doubleMatka * hinta) + " eur");
                if (strTime != null & strDate != null & hinta > 0) {
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
                range = progress;
                rangeValueTextView.setText("Minimum Passanger trip: " + range + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float s = ((intMatka / 100.00f) * progress);
                range = (int) s;
                Log.d("####matka3####", range + ", " + intMatka + ", " + progress + ", " + (intMatka / 100.00f) * progress);
                minRange.setText("PickUp range: " + range + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pass = findViewById(R.id.passengerTxt);

        //Check if passenger count is more than 0
        np.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( passengers <= 1) {
                    findViewById(R.id.minusBtn).setEnabled(false);
                }else findViewById(R.id.minusBtn).setEnabled(true);

                if (np.getText() != null && np.length() > 0) {
                    if (Integer.parseInt(np.getText().toString()) < 1){
                        passengers = 1;
                        findViewById(R.id.minusBtn).setEnabled(false);
                        np.setText(String.valueOf(passengers));
                    }else if(passengers != Integer.parseInt(np.getText().toString())){
                        passengers = Integer.parseInt(np.getText().toString());
                        findViewById(R.id.minusBtn).setEnabled(false);
                        np.setText(String.valueOf(passengers));
                    }

                }
                Log.d("####passengers####", String.valueOf(passengers));
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == txtDate) {
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
        } else if (v == txtTime) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String format = "%1$02d";
                            String estHour = String.format(format, hourOfDay);
                            String estMin = String.format(format, minute);
                            strTime = (estHour + ":" + estMin);
                            txtTime.setText(strTime);
                            pickedHour = hourOfDay;
                            pickedMinute = minute;
                        }
                    }, mHour, mMinute, true);

            timePickerDialog.show();
        } else if (v.getId() == R.id.confirmBtn) {
            if (FirebaseHelper.loggedIn) {
                DatabaseHandler db = new DatabaseHandler();

                mC.set(pickedYear, pickedMonth, pickedDate, pickedHour, pickedMinute);
                long leaveTime = mC.getTimeInMillis();
                //pickUpDistanceEditText = findViewById(R.id.pickUpDistanceEditText);
                String pickUpDistance = pickUpDistanceEditText.getText().toString();
                int pickUpDist = Integer.parseInt(pickUpDistance);

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String duration = newAika;
                //String startDate = strDate;
                //String startTime = strTime;
                String startAddress = newLahtoOs;
                String endAddress = newLoppuOs;
                //int freeSlots = passengers;
                int freeSlots = Integer.parseInt(np.getText().toString());
                float price = hinta;
                List<HashMap<String, String>> points = Constant.pointsList;
                List<String> waypointAddresses = Constant.waypointAddressesList;
                List<String> participants = new ArrayList<>();
                Route route = new Route(uid, duration, leaveTime, startAddress, endAddress,

                        freeSlots, price, doubleMatka, points, waypointAddresses, participants, pickUpDist);

                db.createRide(route, RideDetailsActivity.this);
            } else {
                FirebaseHelper.GoToLogin(getApplicationContext());
            }
        }
    }

    //Pluss and Minus Buttons
    public void addButtons(View v){
        if (np.getText() != null && np.length() > 0) {
            passengers = Integer.parseInt(np.getText().toString());
            if (v == findViewById(R.id.plussBtn)){passengers++;}
            else if (v == findViewById(R.id.minusBtn) ){passengers--;}
        }

        np.setText(String.valueOf(passengers));
    }
}
