package com.frances.badger.data;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BViewModel extends AndroidViewModel {
    private BRepository repository;
    private final LiveData<List<TableEntry>> allBadgers;
    private final List<TableEntry> textBadgers;
    private final List<String> allTitles;

    public BViewModel(Application application) {
        super(application);
        repository = new BRepository(application);
        textBadgers = repository.getTextBadgers();
        allBadgers = repository.getAllBadgers();
        allTitles = repository.getTitles();
    }

    //Returns a cached list of words
    public LiveData<List<TableEntry>> getAllBadgers(){
        return allBadgers;
    }

    public List<TableEntry> getTextBadgers() { return textBadgers;}

    public List<String> getTitles() { return allTitles;}

    public TableEntry getEntryById(long id) { return repository.getEntryById(id); }

    public long insert(TableEntry entry){
        return repository.insert(entry);
    }

    public void deleteEntry(TableEntry entry){ repository.deleteEntry(entry);}

    public void markAsComplete(long id){ repository.markAsComplete(id);}

    public void incrementSnooze(long id) { repository.incrementSnooze(id); }

    public void updateTimeDue(long id){ repository.updateTimeDue(id); }

    public String getTimeDue(long id){
        return repository.getTimeDue(id);
    }

    public String getInitialTimeDue(long id) { return repository.getInitialTimeDue(id); }

    public String getDesc(long id){
        return repository.getDesc(id);
    }

    public int getSnoozeCount(long id){
        return repository.getSnoozeCount(id);
    }
}
