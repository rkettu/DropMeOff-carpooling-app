package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("JOKUKEY");
        Log.d("HALOJATAPAIVAA", user.getFname());

        /*dbHandler = new DatabaseHandler();
        dbHandler.init(FirebaseAuth.getInstance().getCurrentUser());
        user = dbHandler.getProfilepick(user);*/

        Picasso.with(ProfileActivity.this).load(user.getImgUri()).into(profileImageView);
        profileNameTextView.setText(user.getFname() + " " + user.getLname());
        profileEmailTextView.setText(user.getEmail());
        profilePhoNumTextView.setText(user.getPhone());
        profileBioTextView.setText(user.getBio());

    }

    public void editProfile(View v) {gotoEdit();}
    private void gotoEdit(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Exit app with pressing back putton on your phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}
