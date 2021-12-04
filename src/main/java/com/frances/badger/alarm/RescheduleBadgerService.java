package com.frances.badger.alarm;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleService;

import com.frances.badger.MainActivity;
import com.frances.badger.data.BRepository;
import com.frances.badger.data.TableEntry;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RescheduleBadgerService extends LifecycleService {
    List<TableEntry> badgers;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);

        BRepository newRepository = new BRepository(getApplication());


        badgers = newRepository.getTextBadgers();
        int noOfBadgers = badgers.size();
        for(int i=0; i < noOfBadgers; i++){
            BadgerManager.setAlarm(this, badgers.get(i).getId(), badgers.get(i).getBadgerTitle(), badgers.get(i).getTimeDue());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

}
