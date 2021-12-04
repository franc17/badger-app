package com.frances.badger.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
@RequiresApi(api = Build.VERSION_CODES.O)
public class BRepository {
    private BadgerDao badgerDao;
    private LiveData<List<TableEntry>> allBadgers;

    public BRepository(Application application){
        BadgerDatabase bd = BadgerDatabase.getInstance(application);
        badgerDao = bd.badgerDao();
        allBadgers = badgerDao.loadCompletedBadgers(false);
    }

    public LiveData<List<TableEntry>> getAllBadgers(){
        return allBadgers;
    }

    public List<TableEntry> getTextBadgers(){
        Future<List<TableEntry>> ft = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getTextBadgers());

        List<TableEntry> result = null;
        try {
            result = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getTitles(){
        Future<List<String>> ft = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getTitles());

        List<String> result = null;
        try {
            result = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TableEntry getEntryById(long id){
        Future<TableEntry> ft = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getEntryById(id));

        TableEntry result = null;
        try {
            result = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    long insert(TableEntry entry){
        Future<Long> fl = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.insertEntry(entry));

        Long result = null;
        try{
            result = fl.get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    void deleteEntry(TableEntry entry){
        BadgerDatabase.databaseWriteExecutor.execute(() -> badgerDao.deleteEntry(entry));
    }


    public void incrementSnooze(long id){
        BadgerDatabase.databaseWriteExecutor.execute(() -> badgerDao.incrementSnooze(id));
    }

    public void markAsComplete(long id){
        BadgerDatabase.databaseWriteExecutor.execute(() -> badgerDao.markAsComplete(id));
    }

    public void updateTimeDue(long id){
        Future<String> f = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getTimeDue(id));

        String result = null;
        try {
            result = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Future<Integer> i = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getInterval(id));
        Integer intResult = null;
        try {
            intResult = i.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        LocalDateTime temp = LocalDateTime.parse(result);
        temp = temp.plusMinutes(intResult);

        String newTime = temp.toString();

        BadgerDatabase.databaseWriteExecutor.execute(() -> badgerDao.updateTimeDue(id, newTime));
    }



    public String getDesc(long id){
        Future<String> f = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getDesc(id));

        String result = null;
        try {
            result = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTimeDue(long id){
        Future<String> f = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getTimeDue(id));

        String result = null;
        try {
            result = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }



    public String getInitialTimeDue(long id){
        Future<String> f = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getInitialTimeDue(id));

        String result = null;
        try {
            result = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getSnoozeCount(long id){
        Future<Integer> f = BadgerDatabase.databaseWriteExecutor.submit(() -> badgerDao.getSnoozeCount(id));

        Integer result = null;
        try {
            result = f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (int)result;

    }


}
