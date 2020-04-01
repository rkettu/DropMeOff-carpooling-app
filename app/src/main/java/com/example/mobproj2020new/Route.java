package com.example.mobproj2020new;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Route {
    private String uid;
    private String duration;
    //private String startDate;
    //private String startTime;
    private long leaveTime;
    private String startAddress;
    private String endAddress;
    private int freeSlots;
    private float price;
    private List<HashMap<String,String>> points;
    private List<String> waypointAddresses;
    private List<String> participants;


    public Route() {}

    public Route(String uid, String duration, long leaveTime,
                 String startAddress, String endAddress, int freeSlots, float price,
                 List<HashMap<String,String>> points, List<String> waypointAddresses,
                 List<String> participants)
    {
        this.uid = uid;
        this.duration = duration;
        //this.startDate = startDate;
        //this.startTime = startTime;
        this.leaveTime = leaveTime;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.freeSlots = freeSlots;
        this.price = price;
        this.points = points;
        this.waypointAddresses = waypointAddresses;
        this.participants = participants;
    }

    public String getUid() {
        return uid;
    }
    public String getDuration() {
        return duration;
    }
    //public String getStartDate(){return startDate;}
    //public String getStartTime() {return startTime;}
    public long getLeaveTime() { return leaveTime; }

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
    public List<String> getParticipants() { return participants; }

    public void removeFreeSlot() { this.freeSlots--; }

    public void addToParticipants(String userId)
    {
        participants.add(userId);
    }

    public void initParticipants()
    {
        participants = new ArrayList<>();
    }
}

