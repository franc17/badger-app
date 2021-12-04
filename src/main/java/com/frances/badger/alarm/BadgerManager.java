package com.frances.badger.alarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class BadgerManager {
    private BadgerManager(){}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setAlarm(Context context, long badgerId, String title, LocalDateTime due){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        ZonedDateTime zonedDue = due.atZone(ZoneId.of(TimeZone.getDefault().getID()));

        Intent i = new Intent(context, BadgerAlarmReceiver.class);
        i.putExtra("BadgerID", badgerId); //Pass the id as a long
        i.putExtra("BadgerTitle", title);

        int reqCode = (int) badgerId;

        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, zonedDue.toInstant().toEpochMilli(), pi);
    }


}
