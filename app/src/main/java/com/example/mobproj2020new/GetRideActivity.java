package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

public class GetRideActivity extends AppCompatActivity{

    RadioButton luggageCheckBox;
    EditText luggageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ride);

        luggageCheckBox = findViewById(R.id.luggageRadioButton);
        luggageEditText = findViewById(R.id.luggageEditText);
        luggageEditText.setVisibility(View.INVISIBLE);

        luggageCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    luggageEditText.setVisibility(View.VISIBLE);
            }
        });

    }

    public void searchButton(View v){

    }
}
