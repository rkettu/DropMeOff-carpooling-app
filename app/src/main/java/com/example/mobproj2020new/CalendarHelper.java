package com.example.mobproj2020new;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarHelper {

    // DD/MM/YY HH:MM
    public static String getFullTimeString(long timeInMillis)
    {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(timeInMillis);
        String minuteString = c.get(Calendar.MINUTE) < 10 ? ("0" + c.get(Calendar.MINUTE)) : Integer.toString(c.get(Calendar.MINUTE));
        String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+minuteString;
        return timeString;
    }

    // DD/MM/YYYY
    public static String getDateTimeString(long timeInMillis)
    {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(timeInMillis);
        String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
        return timeString;
    }

    // HH:MM
    public static String getHHMMString(long timeInMillis)
    {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(timeInMillis);
        String minuteString = c.get(Calendar.MINUTE) < 10 ? ("0" + c.get(Calendar.MINUTE)) : Integer.toString(c.get(Calendar.MINUTE));
        String timeString = c.get(Calendar.HOUR_OF_DAY)+":"+minuteString;
        return timeString;
    }
}
