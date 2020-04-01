package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    //CircleImageView profileImage;
    EditText fNameEdit;
    EditText lNameEdit;
    EditText eMailEdit;
    EditText cellEdit;
    EditText passEdit;
    EditText passConfEdit;
    ArrayList<EditText> textEditArray;
    Button confButton;
    CheckBox checkTerms;

    //private Uri uData;

    private FirebaseAuth mAuth;
    private DatabaseHandler dbHandler;

    private final String TAG = "CREATEUSERACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();


        //profileImage = findViewById(R.id.profile_image);
        fNameEdit = findViewById(R.id.firstNameedit);
        lNameEdit = findViewById(R.id.lastNameedit);
        eMailEdit = findViewById(R.id.emailedit);
        cellEdit = findViewById(R.id.phoNumedit);
        passEdit = findViewById(R.id.passedit);
        passConfEdit = findViewById(R.id.passtwoedit);
        textEditArray = new ArrayList<>();
        textEditArray.add(fNameEdit);
        textEditArray.add(lNameEdit);
        textEditArray.add(eMailEdit);
        textEditArray.add(cellEdit);
        textEditArray.add(passEdit);
        textEditArray.add(passConfEdit);
        confButton = findViewById(R.id.confirmSignUpButton);
        confButton.setEnabled(false);

        //Check terms of service checkbox
        checkTerms = findViewById(R.id.serviceBox);
        checkTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTerms.isChecked()){
                    confButton.setEnabled(true);
                }else { confButton.setEnabled(false); }
            }
        });
    }


    public void confirmSignUp(View v){
        SignUpFunc();
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
            //uData = data.getData();
            //profileImage.setImageURI(uData);

        }
    }

    private void SignUpFunc()
    {
        final String email = eMailEdit.getText().toString();
        final String pass1 = passEdit.getText().toString();
        final String pass2 = passConfEdit.getText().toString();
        final String phone = cellEdit.getText().toString();
        final String fname = fNameEdit.getText().toString();
        final String lname = lNameEdit.getText().toString();

        boolean entryCheck = true;

        for (EditText et: textEditArray) {
            if (et == null || et.length() == 0) {
                et.setText("");
                et.setHintTextColor(Color.parseColor("#B75252"));
                entryCheck = false;
            }
        }
        if (entryCheck){
            //Check password and confirmation
            if (pass1.equals(pass2)){
                mAuth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fname).build();
                            user.updateProfile(profileUpdates);
                            // Init dbHandler object for this class only
                            dbHandler = new DatabaseHandler();

                            dbHandler.setUserCreationInfo(fname, lname, phone);
                            //dbHandler.putImageToStorage(uData);
                            /*Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                            startActivity(intent);*/
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  Sign in failed...
                        }
                    }
                });
            }else {
                passEdit.setText("");
                passConfEdit.setText("");
                passEdit.setHintTextColor(Color.parseColor("#B75252"));
                passConfEdit.setHintTextColor(Color.parseColor("#B75252"));
                Toast.makeText(this, "Password and confirmation password do not match", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "You need to complete Sign Up", Toast.LENGTH_SHORT).show();
        }
    }
}
