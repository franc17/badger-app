package com.frances.badger.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BadgerDao {
    @Insert
    long insertEntry(TableEntry entry);

    @Delete
    void deleteEntry(TableEntry entry);

    @Update
    void updateEntry(TableEntry entry);

    @Query("SELECT * FROM badger_database")
    public LiveData<List<TableEntry>> loadAllBadgers();

    @Query("SELECT * FROM badger_database WHERE completed = :bool")
    public LiveData<List<TableEntry>> loadCompletedBadgers(boolean bool);

    @Query("SELECT * FROM badger_database WHERE completed = 0")
    public List<TableEntry> getTextBadgers();

    @Query("SELECT badger_desc FROM badger_database")
    public List<String> getTitles();

    //Get an entire table entry by its id
    @Query("SELECT * FROM badger_database WHERE id = :id")
    public TableEntry getEntryById(long id);

    //Incrementing the snooze counter by one
    @Query("UPDATE badger_database SET snooze_count = snooze_count + 1 WHERE id = :id")
    public void incrementSnooze(long id);

    //Update time due
    @Query("UPDATE badger_database SET date_time_due = :newDue WHERE id= :id")
    public void updateTimeDue(long id, String newDue);

    //Mark as complete
    @Query("UPDATE badger_database SET completed = 1 WHERE id = :id")
    public void markAsComplete(long id);

    //Get time due
    @Query("SELECT date_time_due FROM badger_database WHERE id=:id")
    public String getTimeDue(long id);

    //Get initial time due
    @Query("Select initial_date_time_due FROM badger_database WHERE id=:id")
    public String getInitialTimeDue(long id);

    //Get description
    @Query("SELECT badger_desc FROM badger_database WHERE id=:id")
    public String getDesc(long id);

    //Get snooze count
    @Query("SELECT snooze_count FROM badger_database WHERE id=:id")
    public int getSnoozeCount(long id);

    //Get interval
    @Query("SELECT snooze_interval FROM badger_database WHERE id=:id")
    public int getInterval(long id);

}
