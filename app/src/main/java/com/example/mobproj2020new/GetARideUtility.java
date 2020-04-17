package com.example.mobproj2020new;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GetARideUtility implements Serializable {

    private String startAddress;
    private String endAddress;
    private float price;
    private String duration;
    private Long leaveTime;
    private long freeSlots;
    private String uid;
    private List<String> waypointAddresses;
    private List<String> participants;
    private List <HashMap<String, String>> points;
    private String rideId;
    private String startCity;
    private String endCity;

    public GetARideUtility(){}

    public GetARideUtility(String uid, String startAddress, String endAddress, String duration, String rideId, float price, long leaveTime,
                           long freeSlots, List<String> participants ,List<String> waypointAddresses, String startCity, String endCity) {
        this.uid = uid;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.duration = duration;
        this.rideId = rideId;
        this.price = price;
        this.leaveTime = leaveTime;
        this.freeSlots = freeSlots;
        this.waypointAddresses = waypointAddresses;
        this.participants = participants;
        this.startCity = startCity;
        this.endCity = endCity;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public float getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public long getFreeSlots() {
        return freeSlots;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getWaypointAddresses() {
        return waypointAddresses;
    }

    public List<HashMap<String, String>> getPoints() {
        return points;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public String getRideId() { return rideId; }

    public void setRideId(String rideId) { this.rideId = rideId; }
}

