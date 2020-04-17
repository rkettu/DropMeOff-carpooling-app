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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class CustomDialogRatingClass extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button sendBtn;
    public Float rates;
    public String id;
    public String name;
    public String uid;
    User u;
    List<UserData> udList;
    int pos;
    RatingCustomList aa1 = null;
    RatingCustomList2 aa2 = null;

    // Type 1 Constructor: for rating a driver
    public CustomDialogRatingClass(Activity a, User u, String uid, List<UserData> udList,int pos,RatingCustomList aa1){
        super(a);
        this.u = u;
        this.uid = uid;
        this.c = a;
        this.pos = pos;
        this.udList =udList;
        this.aa1 = aa1;
    }
    // Type 2 Constructor: for rating a participant
    public CustomDialogRatingClass(Activity a, User u, String uid, List<UserData> udList,int pos,RatingCustomList2 aa2){
        super(a);
        this.u = u;
        this.uid = uid;
        this.c = a;
        this.pos = pos;
        this.udList =udList;
        this.aa2 = aa2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        final TextView nameTxt = (TextView)findViewById(R.id.txtName);
        final TextView rateTxt = (TextView)findViewById(R.id.txtRate);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        nameTxt.setText("Your rating for " + u.getFname());

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
            float rating = 0;
            int ratingAmount = 0;
            try {
                rating = u.getRating();
                ratingAmount = u.getRatingAmount();
            } catch(Exception e){}
            float totalRating = rating * ratingAmount;
            ratingAmount++;
            float newRating = (totalRating + rates)/(float)ratingAmount;
            u.setRating(newRating);
            u.setRatingAmount(ratingAmount);
            FirebaseFirestore.getInstance().collection("users").document(uid).update("rating",newRating,"ratingAmount",ratingAmount)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d("TASK COMPLETE", "updated user's ratings");
                                if(aa1 != null) // Rated a driver
                                {
                                    udList.remove(pos);
                                    aa1.notifyDataSetChanged();
                                }
                                else if(aa2 != null) // Rated a passenger
                                {
                                    udList.remove(pos);
                                    aa2.notifyDataSetChanged();
                                }
                            }
                        }
                    });
            dismiss();
        }

    }

}
