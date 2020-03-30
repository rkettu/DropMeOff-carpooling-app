package com.example.mobproj2020new;

import android.widget.TextView;

import java.util.ArrayList;

public class TripInfoDummyData {

    private String mHeadline;
    private String mDistance;
    private String mPrice;
    private String mDateOfTrip;
    private String mDuration;
    private String mInfo;

    public TripInfoDummyData(String headline, String distance, String price, String dateOfTrip, String duration, String info){
        mHeadline = headline;
        mDistance = distance;
        mPrice = price;
        mDateOfTrip = dateOfTrip;
        mDuration = duration;
        mInfo = info;
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmDistance() {
        return mDistance;
    }

    public void setmDistance(String mDistance) {
        this.mDistance = mDistance;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDateOfTrip() {
        return mDateOfTrip;
    }

    public void setmDateOfTrip(String mDateOfTrip) {
        this.mDateOfTrip = mDateOfTrip;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmInfo() {
        return mInfo;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }
}
