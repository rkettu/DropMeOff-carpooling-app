package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.startButton).setOnClickListener(this);
        findViewById(R.id.startButton2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.startButton) {
            Intent maps_testi = new Intent(this, MapsActivity.class);
            startActivity(maps_testi);
        }
        else if (v.getId() == R.id.startButton2){
            Intent route = new Intent(this, RouteActivity.class);
            startActivity(route);
        }
    }
}
