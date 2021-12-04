package com.frances.badger.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.frances.badger.BadgerTest;

public class BadgerAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
//        //If the broadcast is a reboot notification
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Toast.makeText(context.getApplicationContext(), "Rebooted", Toast.LENGTH_LONG);
            startRescheduleBadgerService(context);
        }
        //If not, we can assume the notification is the alarm
        else{
            startBadgerService(context, intent);
        }

    }

    public void startBadgerService(Context context, Intent intent){
        Intent intentService = new Intent(context, BadgerService.class);
        intentService.putExtra("BadgerID", intent.getLongExtra("BadgerID", 0));
        intentService.putExtra("BadgerTitle", intent.getStringExtra("BadgerTitle"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    public void startRescheduleBadgerService(Context context){
        Intent intentService = new Intent(context, RescheduleBadgerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}


