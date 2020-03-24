package com.example.mobproj2020new;

import java.io.Serializable;

public class GetARideUtility implements Serializable {
    private String tripLength;
    private String tripTime;
    private String startPoint;
    private String endPoint;
    private String user;

    public GetARideUtility(){

    }

    public GetARideUtility(String tripLength, String tripTime, String startPoint, String endPoint, String user){
        this.tripLength = tripLength;
        this.tripTime = tripTime;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.user = user;
    }

    public String getTripLength() {
        return tripLength;
    }

    public void setTripLength(String tripLength) {
        this.tripLength = tripLength;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
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
