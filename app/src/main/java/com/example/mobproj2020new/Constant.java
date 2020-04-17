package com.example.mobproj2020new;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constant {

    public static int pendingAlarmsAmount = 0;
    public static long MinuteInMillis = 60000;
    public static long HourInMillis = 3600000;
    public static long DayInMillis = 86400000;

    public static String DISTANCE="";
    public static String DURATION="";
    public static List<HashMap<String,String>> pointsList;
    public static List<String> waypointAddressesList = new ArrayList<String>() { {add(""); add("");}};

    public static void emptyPointsList()
    {
        pointsList.clear();
    }

}
