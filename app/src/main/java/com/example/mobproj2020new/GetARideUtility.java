package com.example.mobproj2020new;

import java.io.Serializable;

public class GetARideUtility implements Serializable {

    private String tripDate;
    private String tripStartTime;
    private String tripStartPoint;
    private String tripEndPoint;
    private String tripUser;


    public GetARideUtility(String tripStartPoint, String tripEndPoint, String tripDate, String tripStartTime, String tripUser){
        this.tripDate = tripDate;
        this.tripStartTime = tripStartTime;
        this.tripStartPoint = tripStartPoint;
        this.tripEndPoint = tripEndPoint;
        this.tripUser = tripUser;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(String tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public String getStartPoint() {
        return tripStartPoint;
    }

    public void setStartPoint(String tripStartPoint) {
        this.tripStartPoint = tripStartPoint;
    }

    public String getEndPoint() {
        return tripEndPoint;
    }

    public void setEndPoint(String tripEndPoint) {
        this.tripEndPoint = tripEndPoint;
    }

    public String getTripUser() {
        return tripUser;
    }

    public void setTripUser(String tripUser) {
        this.tripUser = tripUser;
    }
}
