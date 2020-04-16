package com.example.mobproj2020new;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppUser {
    private static String uid;
    private static String imgUri;
    private static String fname;
    private static String lname;
    private static String email;
    private static String phone;
    private static String bio;
    private static List<String> bookedRides;
    private static float rating;
    private static int ratingAmount;

    public static boolean imgSelected;

    public AppUser() {}

    public AppUser (String fN, String lN, String pH, String eM, String bO, String iU, String iD){
        fname = fN;
        lname = lN;
        email = eM;
        phone = pH;
        bio = bO;
        imgUri = iU;
        uid = iD;
    }

    public static void init(){
        String struid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findUserInfo(struid);
    }

    //Get User data
    private static void findUserInfo(final String strUid) {
        FirebaseFirestore myFirestoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFirestoreRef.collection("users").document(strUid);

        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        uid = strUid;
                        imgUri = doc.getString("imgUri");
                        fname = doc.getString("fname");
                        lname = doc.getString("lname");
                        phone = doc.getString("phone");
                        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        bio = doc.getString("bio");
                        try {
                            bookedRides = (List) doc.get("bookedRides");

                        } catch (Exception e) {
                            bookedRides = new ArrayList<>();

                        }
                        try {
                            rating = (float)doc.get("rating");
                            ratingAmount = (int)doc.get("ratingAmount");
                        }catch(Exception e) {
                            rating = 0;
                            ratingAmount = 0;
                        }

                    }
                }
            }
        });
    }

    //Put image to circleImageView
    public static void getImg(final Context context, final CircleImageView circleImageView){
        //GET users (profile) image From FirebaseStorage
        if (imgUri != null) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profpics/" + uid);
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.getResult() != null) {
                        imgUri = String.valueOf(task.getResult());
                        Log.d("######Appuser ImgUri####", String.valueOf(task.getResult()));
                        Picasso.with(context).load(imgUri).into(circleImageView);
                    }
                }
            });
        }else {
            circleImageView.setImageResource(R.drawable.ic_person_add_black_100dp);
            Log.d("######Appuser ImgUri####", "Null");
        }
    }

    //Get image Uri
    public static void setImgUri(){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profpics/" + uid);
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.getResult() != null) {
                        imgUri = String.valueOf(task.getResult());
                        Log.d("######Appuser ImgUri####", String.valueOf(task.getResult()));
                    }
                }
            });
    }

    //Createing user from static Data
    public static User createStaticUser(){
        User user = new User(fname, lname, phone, email, bio, imgUri, uid, bookedRides, 0, 0);
        Log.d("######Appuser check####", "fname " + fname + "\nlname " + lname  + "\nphone " + phone
                + "\nimgUri " + imgUri  + "\nemail " + email + "\nbio " + bio + "\nuid " + uid);
        return user;
    }

    //must be used in SingOut!
    public static void del(){
        uid = null;
        imgUri = null;
        fname = null;
        lname = null;
        phone = null;
        email = null;
        bio = null;
    }

    public static String getUid() { return uid; }
    public static String getUri() { return imgUri; }
    public static String getFname() { return fname; }
    public static String getLname() { return lname; }
    public static String getEmail() { return email; }
    public static String getPhone() { return phone; }
    public static String getBio() { return bio; }
}
