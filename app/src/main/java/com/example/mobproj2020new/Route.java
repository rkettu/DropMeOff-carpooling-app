package com.example.mobproj2020new;

import java.util.HashMap;
import java.util.List;


public class Route{
    private String uid;
    private String duration;
    private String startDate;
    private String startTime;
    private String startAddress;
    private String endAddress;
    private int freeSlots;
    private float price;
    private List<HashMap<String,String>> points;
    private List<String> waypointAddresses;

    public Route() {}

    public Route(String uid, String duration, String startDate, String startTime,
                 String startAddress, String endAddress, int freeSlots, float price,
                 List<HashMap<String,String>> points, List<String> waypointAddresses)
    {
        this.uid = uid;
        this.duration = duration;
        this.startDate = startDate;
        this.startTime = startTime;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.freeSlots = freeSlots;
        this.price = price;
        this.points = points;
        this.waypointAddresses = waypointAddresses;
    }

    public String getUid() {
        return uid;
    }
    public String getDuration() {
        return duration;
    }
    public String getStartDate(){
        return startDate;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getStartAddress() {
        return startAddress;
    }
    public String getEndAddress() {
        return endAddress;
    }
    public int getFreeSlots() {
        return freeSlots;
    }
    public float getPrice() {
        return price;
    }
    public List<HashMap<String,String>> getPoints() {
        return points;
    }
    public List<String> getWaypointAddresses() {
        return waypointAddresses;
    }

}

