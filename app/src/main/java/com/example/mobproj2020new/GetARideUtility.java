package com.example.mobproj2020new;

import java.io.Serializable;

public class GetARideUtility implements Serializable {
    private String tripDate;
    private String tripStartTime;
    private String startPoint;
    private String endPoint;
    private String user;


    public GetARideUtility(String tripDate, String tripStartTime, String startPoint, String endPoint, String user){
        this.tripDate = tripDate;
        this.tripStartTime = tripStartTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.user = user;
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
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
