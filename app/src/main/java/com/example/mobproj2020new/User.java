package com.example.mobproj2020new;

public class User {
    private String fname;
    private String lname;
    private String phone;

    public User() {}

    public User(String fname, String lname, String phone)
    {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
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




}
