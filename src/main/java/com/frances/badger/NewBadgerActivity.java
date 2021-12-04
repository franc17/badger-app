package com.frances.badger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Locale;

import static java.time.format.TextStyle.FULL;


public class NewBadgerActivity extends AppCompatActivity{

    String[] snoozeinterval = {"1 minute", "2 minutes", "5 minutes", "10 minutes"};
    String[] dayList;
    public String info;

    public static final String EXTRA_REPLY = "com.example.android.badger.REPLY";

    private EditText whatBadger;



        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_badger);
        whatBadger = findViewById(R.id.what);

        TimePicker time = findViewById(R.id.timePicker);
        time.setIs24HourView(true);


            //Set snooze dropdown
        Spinner snoozeSpinner = (Spinner) findViewById(R.id.snooze_spinner);
        SnoozeSpinnerClass ss = new SnoozeSpinnerClass();
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, snoozeinterval);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snoozeSpinner.setAdapter(aa);
        snoozeSpinner.setOnItemSelectedListener(ss);


            //Set day of week dropdown
        Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
        DaySpinnerClass ds = new DaySpinnerClass();
        dayList = ds.getDaySpinner();
        ArrayAdapter ab = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dayList);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(ab);
        daySpinner.setOnItemSelectedListener(ds);



        final Button saveNewButton = findViewById(R.id.save_new_button);
        saveNewButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(whatBadger.getText())){
                Toast missingText = Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT);
                missingText.show();
            }
            else {
                info = Integer.toString(time.getHour()) + ":" + Integer.toString(time.getMinute())+ "|" + ss.getInfo() + "|" + ds.getInfo().toString()+ "|" + whatBadger.getText().toString();
                replyIntent.putExtra("Data", info);
                setResult(RESULT_OK, replyIntent);
                finish();

            }
        });
    }

}

@RequiresApi(api = Build.VERSION_CODES.O)
class DaySpinnerClass implements AdapterView.OnItemSelectedListener{
    LocalDate dayInfo;
    LocalDate rightNow = LocalDate.now();


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch(position){
            case 0:
             dayInfo = rightNow;
             break;
            case 1:
                dayInfo = rightNow.plusDays(1);
                break;
            case 2:
                dayInfo = rightNow.plusDays(2);
                break;
            case 3:
                dayInfo = rightNow.plusDays(3);
                break;
            case 4:
                dayInfo = rightNow.plusDays(4);
                break;
            case 5:
                dayInfo = rightNow.plusDays(5);
                break;
            case 6:
                dayInfo = rightNow.plusDays(6);
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        dayInfo = rightNow;
    }

    public LocalDate getInfo(){
        return dayInfo;
    }

    public String[] getDaySpinner(){
        String[] days = new String[7];
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        days[0] = "Today";
        days[1] = "Tomorrow";
        for(int i=2; i<7; i++){
            days[i] = today.plus(i).getDisplayName(FULL, Locale.getDefault());
        }
        return days;
    }
}

class SnoozeSpinnerClass implements AdapterView.OnItemSelectedListener{
    String snoozeInfo = "";

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch(position){
            case 0:
                snoozeInfo = "1";
                break;
            case 1:
                snoozeInfo = "2";
                break;
            case 2:
                snoozeInfo = "5";
                break;
            case 3:
                snoozeInfo = "10";
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        snoozeInfo = "5";

    }
    public String getInfo(){
        return snoozeInfo;
    }
}


