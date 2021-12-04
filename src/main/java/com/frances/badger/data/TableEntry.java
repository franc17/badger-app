package com.frances.badger.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDateTime;
@RequiresApi(api = Build.VERSION_CODES.O)
@Entity(tableName = "badger_database")
public class TableEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name = "badger_desc")
    public String badger_desc;

    @ColumnInfo(name = "date_time_set")
    public String date_time_set;

    @ColumnInfo(name = "date_time_due")
    public String date_time_due;

    @ColumnInfo(name = "snooze_interval")
    public int snooze_interval;

    @ColumnInfo(name = "snooze_count")
    public int snooze_count;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "initial_date_time_due")
    public String initial_date_time_due;

    public TableEntry(String badger_desc, String date_time_set, String date_time_due,
                      int snooze_interval, int snooze_count, boolean completed){
        this.badger_desc = badger_desc;
        this.date_time_set = date_time_set;
        this.date_time_due = date_time_due;
        this.snooze_interval = snooze_interval;
        this.snooze_count = snooze_count;
        this.completed = completed;
        this.initial_date_time_due = date_time_due;
    }



    public String getBadgerTitle() {
        return this.badger_desc;
    }
    public String getStringTimeDue(){return date_time_due;}
    public long getId() { return this.id;}

    public LocalDateTime getTimeDue(){
        LocalDateTime result = LocalDateTime.parse(this.date_time_due);
        return result;
    }

}
