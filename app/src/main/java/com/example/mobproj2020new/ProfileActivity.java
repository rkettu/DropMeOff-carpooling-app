package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    StorageReference mDataRef;
    DatabaseHandler dbHandler;

    CircleImageView profileImageView;
    TextView profileNameTextView;
    TextView profileEmailTextView;
    TextView profilePhoNumTextView;
    TextView profileBioTextView;
    TextView profileRatingTextView;
    RatingBar rating;
    User user;

    String struri = "DEF_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView = findViewById(R.id.profile_image);
        profileNameTextView = findViewById(R.id.profileNameText);
        profileEmailTextView = findViewById(R.id.profileEmailText);
        profilePhoNumTextView= findViewById(R.id.profilePhoNumText);
        profileBioTextView = findViewById(R.id.profileBioText);
        profileRatingTextView = findViewById(R.id.profileRatingText);
        rating = findViewById(R.id.ratingBar);


        Intent i = getIntent();
        user = (User) i.getSerializableExtra("JOKUKEY");
        Log.d("HALOJATAPAIVAA", user.getFname());

        /*dbHandler = new DatabaseHandler();
        dbHandler.init(FirebaseAuth.getInstance().getCurrentUser());
        user = dbHandler.getProfilepick(user);*/

        Picasso.with(ProfileActivity.this).load(user.getImgUri()).into(profileImageView);
        //AppUser.getImg(getApplicationContext(), profileImageView);
        profileNameTextView.setText(user.getFname() + " " + user.getLname());
        profileEmailTextView.setText(user.getEmail());
        profilePhoNumTextView.setText(user.getPhone());
        profileBioTextView.setText(user.getBio());

        if(!user.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            findViewById(R.id.editProfileBtn).setVisibility(View.GONE);
            getRating(user.getUid());
        }
        else{
            Log.d("TAG", "onCreate: " + AppUser.getUid());
            getRating(AppUser.getUid());
        }
    }

    private void getRating(String uid){
        FirebaseFirestore myFireStoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFireStoreRef.collection("users").document(uid);
        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    //TODO: if myRating is integer, crashes
                    double myRating = (double) doc.get("rating");
                    long myRatingAmount = (long) doc.get("ratingAmount");
                    String myNewRatingAmount = String.valueOf(myRatingAmount);
                    String myNewRating = String.valueOf(myRating);
                    float newestRating = Float.parseFloat(myNewRating);
                    rating.setRating(newestRating);
                    profileRatingTextView.setText("(" + myNewRatingAmount + ")");
                }
            }
        });
    }

    public void editProfile(View v) {
        gotoEdit();
    }
    private void gotoEdit(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    ///Back and exit/quit activity///////////
    public void backArrow(View v){
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}
