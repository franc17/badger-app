package com.frances.badger;

import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
@RequiresApi(api = Build.VERSION_CODES.O)
public class NewVoiceActivity extends AppCompatActivity {
    LocalDate today;
    String description;
    TextToSpeech textToSpeech;
    final String request = "request";
    TextView textView;
    final String[] dataForAlarm = new String[5]; //Use array to collect data from each conversational element in the correct order;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_voice);
        today = LocalDate.now();


        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        textView = findViewById(R.id.voice_textview);
        textView.setText(description);
        dataForAlarm[4] = description;



        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.speak("Set badger to "+ description, QUEUE_FLUSH, null, "description");
                    askHour(textToSpeech);
                }
            }
        });





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int codeReceived = data.getIntExtra("Code Received", 0);
        String speechReceived = data.getStringExtra("Speech Received");
        if(resultCode == RESULT_OK){
            if(codeReceived == 101){ //The user was asked about an hour
                dataForAlarm[0] = speechReceived;
                askMinute(textToSpeech);
            }
            else if(codeReceived == 202){
                dataForAlarm[1] = speechReceived;
                askSnooze(textToSpeech);
            }
            else if(codeReceived == 303){
                dataForAlarm[2] = speechReceived;
                askDate(textToSpeech);
            }
            else if(codeReceived == 404){
                if(speechReceived.equalsIgnoreCase("today")){
                    dataForAlarm[3] = today.toString();

                }
                else if(speechReceived.equalsIgnoreCase("tomorrow")){
                    dataForAlarm[3] = today.plusDays(1).toString();
                }
                //Assume that any other day spoken is the day after tomorrow due to specific question wording
                else if(speechReceived.equalsIgnoreCase("monday") || speechReceived.equalsIgnoreCase("tuesday")||speechReceived.equalsIgnoreCase("wednesday")||speechReceived.equalsIgnoreCase("thursday")||speechReceived.equalsIgnoreCase("friday")||speechReceived.equalsIgnoreCase("saturday")||speechReceived.equalsIgnoreCase("sunday")){
                    dataForAlarm[3] = today.plusDays(2).toString();
                }
                //In case of error - try again
                else{
                    textToSpeech.speak("Let's try again", QUEUE_FLUSH, null, "date");
                    askDate(textToSpeech);
                }
                //The arraylist should now be full and the data can be parsed and sent to main to set the alarm
                sendToSave(dataForAlarm);
            }
            else if(codeReceived == 0){//Failure code
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                finish();
            }
        }
    }

    //Have to have a class for each and not a switch because you need a new progress listener each time
    public void askHour(TextToSpeech textToSpeech){
        UtteranceProgressListener listener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Intent intent = new Intent(getApplicationContext(), VoiceListenerActivity.class);
                intent.putExtra(request, "hour");
                startActivityForResult(intent, 101);
            }

            @Override
            public void onError(String s) {
                textToSpeech.speak(getString(R.string.conversation_error), QUEUE_FLUSH, null, "error");
                askHour(textToSpeech);
            }
        };
        textToSpeech.setOnUtteranceProgressListener(listener);
        textToSpeech.speak("What hour in 24 hour clock?", QUEUE_FLUSH, null, "hour");
    }

    public void askMinute(TextToSpeech textToSpeech){
        UtteranceProgressListener listener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Intent intent = new Intent(getApplicationContext(), VoiceListenerActivity.class);
                intent.putExtra(request, "minute");
                startActivityForResult(intent, 202);
            }

            @Override
            public void onError(String s) {
                textToSpeech.speak(getString(R.string.conversation_error), QUEUE_FLUSH, null, "error");
                askMinute(textToSpeech);
            }
        };
        textToSpeech.setOnUtteranceProgressListener(listener);
        textToSpeech.speak("What minute past the hour?", QUEUE_FLUSH, null, "hour");
    }

    public void askSnooze(TextToSpeech textToSpeech){
        UtteranceProgressListener listener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Intent intent = new Intent(getApplicationContext(), VoiceListenerActivity.class);
                intent.putExtra(request, "snooze");
                startActivityForResult(intent, 303);
            }

            @Override
            public void onError(String s) {
                textToSpeech.speak(getString(R.string.conversation_error), QUEUE_FLUSH, null, "error");
                askSnooze(textToSpeech);
            }
        };
        textToSpeech.setOnUtteranceProgressListener(listener);
        textToSpeech.speak("1, 2, 5 or 10 minute snooze interval?", QUEUE_FLUSH, null, "hour");
    }

    public void askDate(TextToSpeech textToSpeech){
        String dayAfterTomorrow = today.plusDays(2).getDayOfWeek().name();
        UtteranceProgressListener listener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                Intent intent = new Intent(getApplicationContext(), VoiceListenerActivity.class);
                intent.putExtra(request, "date");
                intent.putExtra("day after tomorrow", dayAfterTomorrow);
                startActivityForResult(intent, 404);
            }

            @Override
            public void onError(String s) {
                textToSpeech.speak(getString(R.string.conversation_error), QUEUE_FLUSH, null, "error");
                askDate(textToSpeech);

            }
        };
        textToSpeech.setOnUtteranceProgressListener(listener);
        textToSpeech.speak("Today, tomorrow, or " + dayAfterTomorrow, QUEUE_FLUSH, null, "date"); //Limit the options for ease of use and development
    }


    public void sendToSave(String[] data){
        String finalData = data[0].trim() + ":" + data[1].trim() + "|" + data[2].trim() + "|" + data[3].trim() + "|" + data[4].trim();
        Intent finalIntent = new Intent();
        finalIntent.putExtra("Data", finalData);
        setResult(RESULT_OK, finalIntent);
        finish();
    }


}
