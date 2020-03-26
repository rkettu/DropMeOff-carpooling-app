package com.example.mobproj2020new;

import java.io.Serializable;

public class RideDetailPart implements Serializable {


    public String date;
    public String time;
    public int passenger;
    public float price;
    public int range;

    public RideDetailPart(String date, String time, int passenger, float price, int range)
    {
        this.date = date;
        this.time = time;
        this.passenger = passenger;
        this.price = price;
        this.range = range;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRange() { return range; }

    public void setRange(int range) { this.range = range; }

}
