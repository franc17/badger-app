package com.frances.badger.data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.O)

public class TableEntryBuilder {
    private String desc;
    private String set = LocalDateTime.now().toString();
    private String due;
    private int snooze_interval = 5;
    private int snooze_count = 0;
    private boolean completed = false;

    public TableEntryBuilder(){ }

    public TableEntry buildEntry(){
        return new TableEntry(desc, set, due, snooze_interval, snooze_count, completed);
    }

    public TableEntryBuilder description(String desc){
        this.desc = desc;
        return this;
    }

    public TableEntryBuilder due(LocalDateTime due){
        this.due = due.toString();
        return this;
    }

    public TableEntryBuilder interval(int interval){
        this.snooze_interval = interval;
        return this;
    }

}
