package com.frances.badger.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.frances.badger.BadgerSnoozeActivity;
import com.frances.badger.BadgerTest;
import com.frances.badger.R;


public class BadgerService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate(){
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alert);
        mediaPlayer.start();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Intent runSnoozeIntent = new Intent(this, BadgerSnoozeActivity.class);
        runSnoozeIntent.putExtra("BadgerID", intent.getLongExtra("BadgerID", 0));
        runSnoozeIntent.putExtra("BadgerTitle", intent.getStringExtra("BadgerTitle"));
        runSnoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int idInt = (int) intent.getLongExtra("BadgerID", 0);

        PendingIntent pendingSnoozeIntent = PendingIntent.getActivity(this, 0, runSnoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.badgerface)
                .setContentTitle("Badger")
                .setContentText("Badger Notification")
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(pendingSnoozeIntent, true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("notification", "Badger Channel", NotificationManager.IMPORTANCE_MAX);
            manager.createNotificationChannel(channel);
            builder.setChannelId("notification");
        }

        Notification newNotification = builder.build();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "badger:waketag");


        wakeLock.acquire(3000);
        wakeLock.release();
        startForeground(idInt, newNotification);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

}
