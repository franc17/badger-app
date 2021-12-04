package com.frances.badger;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.frances.badger.alarm.BadgerManager;
import com.frances.badger.data.BViewModel;
import com.frances.badger.data.TableEntry;
import com.frances.badger.data.TableEntryBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static final int NEW_BADGER_REQUEST_CODE = 1;
    public static BViewModel bViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bViewModel = new ViewModelProvider(this).get(BViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent received = getIntent();
        if(received.getAction() == "android.intent.action.VIEW"){
            Intent forwardIntent = new Intent(this, NewVoiceActivity.class);
            forwardIntent.putExtra("description", received.getStringExtra("description"));
            startActivityForResult(forwardIntent, NEW_BADGER_REQUEST_CODE);
        }

        Button badgerButton = findViewById(R.id.badger_button);
        badgerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewBadgerActivity.class);
            startActivityForResult(intent, NEW_BADGER_REQUEST_CODE);
        });

        Button viewBadgersButton = findViewById(R.id.view_badgers_button);
        viewBadgersButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                viewBadgers();
//                testButton();

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BADGER_REQUEST_CODE && resultCode == RESULT_OK) {
            TableEntry entry = parseData(data.getStringExtra("Data"));
            long entryId = bViewModel.insert(entry);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            BadgerManager.setAlarm(getApplicationContext(), entryId, entry.getBadgerTitle(), entry.getTimeDue());
        }
        else{
            Toast.makeText(getApplicationContext(), "Not saved", Toast.LENGTH_LONG).show();
        }
    }


    private void viewBadgers(){
        Intent viewIntent = new Intent(this, SeeBadgers.class);
        startActivity(viewIntent);
    }

    //Change this code as needed to trigger specific activities or features
    private void testButton(){
        Intent testIntent = new Intent(this, NewVoiceActivity.class);
        testIntent.putExtra("description", "walk the dog");
        startActivityForResult(testIntent, NEW_BADGER_REQUEST_CODE);
    }

    private TableEntry parseData(String data){
        String[] dataArray = data.split("\\|");
        String[] timeSplit = dataArray[0].split(":");
        LocalTime time = getTime(timeSplit[0], timeSplit[1]);
        LocalDate date = LocalDate.parse(dataArray[2]);
        LocalDateTime due = LocalDateTime.of(date, time);

        TableEntryBuilder builder = new TableEntryBuilder();
        TableEntry result = builder
                .description(dataArray[3])
                .due(due)
                .interval(Integer.parseInt(dataArray[1]))
                .buildEntry();
        return result;
    }

    private LocalTime getTime(String h, String m){
        int hour = Integer.parseInt(h);
        int min = Integer.parseInt(m);
        return LocalTime.of(hour, min);
    }

//    data[0] = hh:mm
    //data[1] = snooze interval
    //data[2] = day
    //data[3] = description

    //static LocalDateTime	of(LocalDate date, LocalTime time)
    //  Obtains an instance of LocalDateTime from a date and time.




}