package com.example.mobproj2020new;

import android.net.Uri;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String imgUri;
    private String fname;
    private String lname;
    private String phone;
    private String bio;
    private boolean profileCreated;

    public User()  {}

    public User(String fname, String lname, String phone)
    {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.profileCreated = false;
    }

    public String getUid() { return uid; }
    public String getImgUri() { return imgUri; }

    public String getBio() { return imgUri; }

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

    public void setBio(String strBio) {bio = strBio;}
}
