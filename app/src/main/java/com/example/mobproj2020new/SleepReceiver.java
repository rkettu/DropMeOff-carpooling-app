package com.example.mobproj2020new;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SleepReceiver extends BroadcastReceiver {

    public static void setAlarm(Context context, long timeInMillis, String title, String text) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SleepReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("TEXT", text);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, Constant.pendingAlarmsAmount, intent, 0);
        Constant.pendingAlarmsAmount++;
        am.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Constant.pendingAlarmsAmount > 0) Constant.pendingAlarmsAmount--;
        String channelId = "my_ch";
        new NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_HIGH);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.my_icon) // TODO: Fix showing icon
                .setContentTitle(intent.getStringExtra("TITLE"))
                .setContentText(intent.getStringExtra("TEXT"))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat mngc = NotificationManagerCompat.from(context);
        mngc.notify(1,mBuilder.build());
    }
}
