package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


// TODO: saving info from profile to database, getting info from database to fill fname, lname etc.
public class EditProfileActivity extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private Uri uData;

    CircleImageView profileImage;
    EditText fNameEdit;
    EditText lNameEdit;
    EditText eMailEdit;
    EditText cellEdit;
    EditText bioEdit;
    EditText passEdit;
    EditText passConfEdit;

    ArrayList<EditText> textEditArray;

    private FirebaseAuth mAuth;
    private DatabaseHandler db;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        AppUser.init();
        Log.d("######EditProfile ImgUri####", "Uri: " + AppUser.getUri());

        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHandler();

        profileImage = findViewById(R.id.profile_image);
        fNameEdit = findViewById(R.id.firstNameedit);
        lNameEdit = findViewById(R.id.lastNameedit);
        eMailEdit = findViewById(R.id.emailedit);
        cellEdit = findViewById(R.id.phoNumedit);
        bioEdit = findViewById(R.id.bioedit);
        passEdit = findViewById(R.id.passedit);
        passConfEdit = findViewById(R.id.passtwoedit);

        textEditArray = new ArrayList<>();
        textEditArray.add(fNameEdit);
        textEditArray.add(lNameEdit);
        textEditArray.add(eMailEdit);
        textEditArray.add(cellEdit);
        textEditArray.add(bioEdit);



        applyButton = (Button) findViewById(R.id.saveProfileDetailButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean entryCheck = true;
                for (EditText item: textEditArray)
                {
                    if (item == null || item.length() == 0) {
                        item.setText("");
                        item.setHintTextColor(Color.parseColor("#B75252"));
                        entryCheck = false;
                    }else {
                        AppUser appUser = new AppUser(
                                fNameEdit.getText().toString(),
                                lNameEdit.getText().toString(),
                                cellEdit.getText().toString(),
                                eMailEdit.getText().toString(),
                                bioEdit.getText().toString(),
                                AppUser.getUri(),
                                AppUser.getUid()
                        );

                        Log.d("######EditProfile check####", "fname " + AppUser.getFname() + "\nlname " + AppUser.getLname()  + "\nphone "
                                + AppUser.getPhone() + "\nimgUri " + AppUser.getUri()  + "\nemail " + AppUser.getEmail()
                                + "\nbio " + AppUser.getBio() + "\nuid " + AppUser.getUid());
                    }
                }
                if (entryCheck || AppUser.imgSelected){

                    // TODO: Maybe add checks that need to be met for profile creation success
                    FirebaseHelper.loggedIn = true;

                    db.setProfileCreated(true);
                    db.updateUserInfo(getApplicationContext());
                    //startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                }

            }
        });

        //Log.d("######editprofile ImgUri####", AppUser.getUri() + AppUser.getUri().length());
        if (AppUser.getUri() != null && AppUser.getUri().length() > 20){
            Picasso.with(EditProfileActivity.this).load(AppUser.getUri()).into(profileImage);
        }else {
            AppUser.getImg(getApplicationContext(), profileImage);
        }




        fNameEdit.setText(AppUser.getFname());
        lNameEdit.setText(AppUser.getLname());
        eMailEdit.setText(AppUser.getEmail());
        cellEdit.setText(AppUser.getPhone());
        bioEdit.setText(AppUser.getBio());
    }

/////////////Check permissions for picking images from phones external storage. Nothing else below/////////////////

    public void pickImage(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permissions not granted
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_CODE);
            }else{
                //permissions granted
                pickImageFromGallery();
            }
        }else {
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else {
                    Toast.makeText(this, "permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            uData = data.getData();
            profileImage.setImageURI(data.getData());
            db.putImageToStorage(uData);
            AppUser.imgSelected = true;
        }
    }
}

















