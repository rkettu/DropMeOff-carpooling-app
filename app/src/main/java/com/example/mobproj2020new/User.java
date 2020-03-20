package com.example.mobproj2020new;

public class User {
    private String fname;
    private String lname;
    private String phone;
    private boolean profileCreated;

    public User() {}

    public User(String fname, String lname, String phone)
    {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.profileCreated = false;
    }


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

}
