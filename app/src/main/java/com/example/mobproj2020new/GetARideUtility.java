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
    private int freeSlots;
    private String uid;
    private List<String> waypointAddresses;
    private List <HashMap<String, String>> points;

    private String newName;
    private String newUri;

    public GetARideUtility(){}

    public GetARideUtility(String newName, String newUri) {
        this.newName = newName;
        this.newUri = newUri;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewUri() {
        return newUri;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setNewUri(String newUri) {
        this.newUri = newUri;
    }

    public GetARideUtility(String tripaUser, String startAddress, String endAddress, String tripDuration, Long leaveTime, int freeSlots, float price) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.freeSlots = freeSlots;
        this.uid = tripaUser;
        this.duration = tripDuration;
        this.price = price;
        this.leaveTime = leaveTime;
    }

    public static ArrayList<GetARideUtility> arrayList = new ArrayList<>();

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getFreeSlots() {
        return freeSlots;
    }

    public void setFreeSlots(int freeSlots) {
        this.freeSlots = freeSlots;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getWaypointAddresses() {
        return waypointAddresses;
    }

    public void setWaypointAddresses(List<String> waypointAddresses) {
        this.waypointAddresses = waypointAddresses;
    }

    public List<HashMap<String, String>> getPoints() {
        return points;
    }

    public void setPoints(List<HashMap<String, String>> points) {
        this.points = points;
    }

    public Long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Long leaveTime) {
        this.leaveTime = leaveTime;
    }
}

