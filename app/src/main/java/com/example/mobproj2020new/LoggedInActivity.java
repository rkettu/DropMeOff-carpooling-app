package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;

public class LoggedInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView ppImgView;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final Button signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                ReturnToLogin();
            }
        });

        db = new DatabaseHandler();
        db.init(mUser);

        ppImgView = (ImageView) findViewById(R.id.profPicImageView);

        final Button ppBtn = (Button) findViewById(R.id.uploadBtn);
        ppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProfilePicture();
            }
        });

        TextView userInfo = (TextView)findViewById(R.id.userInfoTextView);
        userInfo.setText(mUser.getDisplayName()+"\n"+mUser.getEmail()+"\n");


    }

    private void ReturnToLogin()
    {
        // Currently returns only to previous...
        this.finish();
    }

    public void UploadProfilePicture()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto, DatabaseHandler.REQKEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == DatabaseHandler.REQKEY)
        {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ppImgView.setImageBitmap(selectedImage);
                db.putImageToStorage(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
