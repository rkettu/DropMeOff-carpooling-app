package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class RideDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private String strDate;
    private String strTime;
    private int passengers;
    private float hinta;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    SeekBar seekBar;
    TextView hintaTxt;
    TextView exampleTxt;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        NumberPicker np = findViewById(R.id.numberPicker);
        passengers = np.getValue();
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

        seekBar=(SeekBar)findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hinta = ((float) progress / 1000);
                hintaTxt.setText("" + hinta + " per kilometer");
                exampleTxt.setText("Example: between Jyväskylä - Helsinki ( 270km ): \n" + 270 * hinta + " eur");

                if(txtDate.toString().trim().length() > 0 & txtTime.toString().trim().length() > 0)
                {
                    Button confirmBtn = (Button) findViewById(R.id.confirmBtn);
                    confirmBtn.setEnabled(true);
                }
                else
                {
                    confirmBtn.setEnabled(false);
                }
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
                        }
                    }, mHour, mMinute, true);

            timePickerDialog.show();
        }
        else if (v.getId() == R.id.confirmBtn)
        {
            RideDetailPart part = new RideDetailPart(strDate, strTime, passengers, hinta);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("DETAILS", part);
            setResult(RideDetailsActivity.RESULT_OK, resultIntent);
            finish();
        }
    }

}
