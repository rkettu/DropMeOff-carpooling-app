package com.example.mobproj2020new;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarHelper {
    public static String getTimeString(long timeInMillis)
    {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(timeInMillis);
        String minuteString = c.get(Calendar.MINUTE) < 10 ? ("0" + c.get(Calendar.MINUTE)) : Integer.toString(c.get(Calendar.MINUTE));
        String timeString = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+minuteString;
        return timeString;
    }
}
