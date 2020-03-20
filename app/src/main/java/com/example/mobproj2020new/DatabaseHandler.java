package com.example.mobproj2020new;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// Maybe make everything static?
public class DatabaseHandler extends AppCompatActivity {
    private DocumentReference mDocRef;
    private String uid;
    private String FNAMEKEY = "fname";
    private String LNAMEKEY = "lname";
    private String PHONEKEY = "phone";
    private String mPhone = "empty";
    public static final int REQKEY = 1212;
    private StorageReference storageRef;
    private FirebaseStorage fbs;


    public void init(FirebaseUser user)
    {
        uid = user.getUid();
        fbs = FirebaseStorage.getInstance();
        storageRef = fbs.getReference();
        mDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
    }

    public void setUserCreationInfo(String fname, String lname, String phone)
    {
        User user = new User(fname,lname,phone);
        mDocRef.set(user); // add on success, on failure event listeners for checking for errors if needed
    }

    public void getPhone()
    {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        // Creating User object based on document and getting phone string
                        String phone = doc.toObject(User.class).getPhone();   // Currently only printing this value
                        Log.d("HALOOOO", ("phone number from db: " + phone));
                    }
                }
            }
        });
    }



    public void putImageToStorage(Uri imageUri)
    {
        StorageReference ppRef = storageRef.child("profpics/"+uid);
        UploadTask upTask = ppRef.putFile(imageUri);
        // Add listeners for fail/complete/success?
    }
}
