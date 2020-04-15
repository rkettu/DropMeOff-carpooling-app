package com.example.mobproj2020new;

import android.net.Uri;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String uid;
    private String imgUri;
    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String bio;
    private boolean profileCreated;
    private List<String> bookedRides;
    private float rating;
    private int ratingNumber;

    public User()  {}

    public User(String fname, String lname, String phone)
    {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.profileCreated = false;
    }

    //Used on AppUser to create user from Static data
    public User(String fname, String lname, String phone, String email, String bio, String imgUri, String uid, List<String> bookedRides, float rating, int ratingNumber)
    {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.imgUri = imgUri;
        this.uid = uid;
        this.bookedRides = bookedRides;
        this.rating = 0;
        this.ratingNumber = 0;
    }

    public static ArrayList<User> arrayList = new ArrayList<>();

    public static String picUri = "DEF_URI";

    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }
    public String getEmail() { return email; }
    public String getPhone() {
        return phone;
    }
    public String getBio() { return bio; }
    public String getImgUri() { return imgUri; }
    public String getUid() { return uid; }
    public List<String> getBookedRides() { return bookedRides; }
    public Float getRating() { return rating; }
    public int getRatingNumber() { return ratingNumber; }

    public boolean getProfileCreated() { return profileCreated; }

    public void setProfCreated(boolean value) { profileCreated = value; }

    public void setUid(String strUid){uid = strUid;}

    public void setImgUid(String strUri){imgUri = strUri;}

    public void addToBookedRides(String routeId)
    {
        bookedRides.add(routeId);
    }
    
    public void initBookedRides()
    {
        bookedRides = new ArrayList<>();
    }

    public void setRating(Float rating) { this.rating = rating; }

    public void setRatingNumber(int ratingNumber) { this.ratingNumber = ratingNumber; }
}
