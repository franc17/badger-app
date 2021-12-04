package com.frances.badger;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import com.frances.badger.alarm.BadgerAlarmReceiver;
import com.frances.badger.data.BadgerListAdapter;
import com.frances.badger.data.TableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SeeBadgers extends AppCompatActivity {

    private ListView listView;
    private ArrayList<TableEntry> entries = new ArrayList<>();
    private List<TableEntry> tableEntries;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        listView = (ListView) findViewById(R.id.simpleListView);
        //TODO get and display a live version of the table so the viewbadgers list updates automatically
        tableEntries = MainActivity.bViewModel.getTextBadgers(); //Gets Badgers where completed = 0
        entries = (ArrayList<TableEntry>) tableEntries;
        BadgerListAdapter adapter = new BadgerListAdapter(this, R.layout.list_item, entries);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TableEntry entry = adapter.getItem(i);
                        deleteSelectedEntry(entry);
                    }
                });
    }

        public void deleteSelectedEntry(TableEntry entry){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SeeBadgers.this);
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Delete Badger " + entry.badger_desc + "?");
            OnClickListener yesListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.bViewModel.deleteEntry(entry); //Deletes from database
                    dialogInterface.dismiss();
                    Intent intent = new Intent(getApplicationContext(), BadgerAlarmReceiver.class);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), (int) entry.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if(pi!= null){
                        alarmManager.cancel(pi);
                    } //Cancels system alarm
                    finish();
                    Toast.makeText(getApplicationContext(), "Badger deleted", Toast.LENGTH_SHORT).show();
                }
            };
            OnClickListener noListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", yesListener);
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", noListener);
            alert.show();
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tableEntries = MainActivity.bViewModel.getTextBadgers();
        entries = (ArrayList<TableEntry>) tableEntries;
    }
}