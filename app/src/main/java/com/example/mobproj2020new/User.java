package com.example.mobproj2020new;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String uid;
    private String imgUri;
    private String fname;
    private String lname;
    private String phone;
    private boolean profileCreated;

    public User()  {}

    public User(String fname, String lname, String phone)
    {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.profileCreated = false;
    }

    public static ArrayList<User> arrayList = new ArrayList<>();

    public static String picUri = "DEF_URI";

    public String getUid() { return uid; }

    public String getImgUri() { return imgUri; }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public boolean getProfileCreated() { return profileCreated; }

    public void setProfCreated(boolean value) { profileCreated = value; }

    public void setUid(String strUid){uid = strUid;}
    public void setImgUid(String strUri){imgUri = strUri;}

}
