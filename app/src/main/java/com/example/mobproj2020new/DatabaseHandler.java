package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// Maybe make everything static?
public class DatabaseHandler {
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

    public void checkProfileCreated(Context context)
    {
        final Context varContext = context;
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        // Creating User object based on document and getting bool
                        boolean created = doc.toObject(User.class).getProfileCreated();
                        if(created)
                        {
                            Intent intent = new Intent(varContext, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            varContext.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(varContext, EditProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            varContext.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void setProfileCreated(final boolean value)
    {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    User user = doc.toObject(User.class);
                    user.setProfCreated(value);
                    mDocRef.set(user);
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
