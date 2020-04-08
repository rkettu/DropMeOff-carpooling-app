package com.example.mobproj2020new;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogRatingClass extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button sendBtn;
    public Float rates;
    public String id;
    public String name;

    public CustomDialogRatingClass(Activity a, String uid, String fname){
        super(a);
        this.c = a;
        this.id = uid;
        this.name = fname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        final TextView nameTxt = (TextView)findViewById(R.id.txtName);
        final TextView rateTxt = (TextView)findViewById(R.id.txtRate);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        nameTxt.setText("Your rating for " + name);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateTxt.setText("" + rating);
                rates = rating;
            }
        });

        sendBtn = (Button)findViewById(R.id.ratingBtn);
        sendBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ratingBtn){
            Log.d("TESTIII", "haettuuiiiiiiid:" + id);


            c.finish();
        }

    }

}
