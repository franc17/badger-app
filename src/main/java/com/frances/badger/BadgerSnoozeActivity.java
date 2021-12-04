package com.frances.badger;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.frances.badger.alarm.BadgerManager;
import com.frances.badger.alarm.BadgerService;
import com.frances.badger.data.TableEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.speech.tts.TextToSpeech.Engine.KEY_PARAM_VOLUME;
import static android.speech.tts.TextToSpeech.QUEUE_ADD;
import static android.speech.tts.TextToSpeech.QUEUE_FLUSH;


@RequiresApi(api = Build.VERSION_CODES.O)
public class BadgerSnoozeActivity extends AppCompatActivity {
    Vibrator vibrator;
    long badgerId;
    TextToSpeech textToSpeech;
    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    UtteranceProgressListener listener;
    String speech;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Get badger title and id from intent passed by BadgerService
        Intent intent = getIntent();
        badgerId = intent.getLongExtra("BadgerID", 0);

        // Cancel the status bar notification when in snooze screen
        int badgerInt = (int) badgerId;

        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.cancel(badgerInt);


        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setContentView(R.layout.badger_snooze);

        TextView badgerDescText = findViewById(R.id.badger_description);
        Button yesButton = findViewById(R.id.yes_snooze_button);
        Button noButton = findViewById(R.id.no_snooze_button);
        TextView timeDueText = findViewById(R.id.time_due);
        TextView snoozeCountText = findViewById(R.id.snooze_count);

        setDescription(badgerId, badgerDescText);
        setTimeDueText(badgerId, timeDueText);
        setSnoozeCount(badgerId, snoozeCountText);

        speech = "Did you " + MainActivity.bViewModel.getDesc(badgerId);

        askQuestion(speech);


        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bViewModel.incrementSnooze(badgerId);
                MainActivity.bViewModel.updateTimeDue(badgerId);
                TableEntry current = MainActivity.bViewModel.getEntryById(badgerId);
                BadgerManager.setAlarm(getApplicationContext(), badgerId, current.getBadgerTitle(), current.getTimeDue());
                textToSpeech.shutdown();
                finish();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bViewModel.markAsComplete(badgerId);
                Intent intentService = new Intent(getApplicationContext(), BadgerService.class);
                getApplicationContext().stopService(intentService);
                textToSpeech.shutdown();
                finish();
            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        vibrator.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String question = speech;
        String s = data.getStringExtra("Speech");
        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                    if (s.toUpperCase().contains("YES")) {
                        MainActivity.bViewModel.markAsComplete(badgerId);
                        Intent intentService = new Intent(getApplicationContext(), BadgerService.class);
                        getApplicationContext().stopService(intentService);
                        textToSpeech.shutdown();
                        finish();
                    } else if (s.toUpperCase().contains("NO")) {
                        MainActivity.bViewModel.incrementSnooze(badgerId);
                        MainActivity.bViewModel.updateTimeDue(badgerId);
                        TableEntry current = MainActivity.bViewModel.getEntryById(badgerId);
                        BadgerManager.setAlarm(getApplicationContext(), badgerId, current.getBadgerTitle(), current.getTimeDue());
                        textToSpeech.shutdown();
                        finish();
                    } else {
                        askQuestion(question);
                    }
            }
        }
    }

    public void askQuestion(String question){
        listener = new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onDone(String s) {
                Intent speechIntent = new Intent(getApplicationContext(), SpeechRecognizerActivity.class);
                speechIntent.putExtra("Question", question);
                startActivityForResult(speechIntent, REQUEST_SPEECH_RECOGNIZER);
            }

            @Override
            public void onError(String s) {
            }
        };
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                    textToSpeech.setOnUtteranceProgressListener(listener);
                    textToSpeech.speak(question, QUEUE_FLUSH, null, "id");
                }
            }
        });
    }

    //    public RecognitionListener getListener(String question){
//        return new RecognitionListener() {
//            @Override
//            public void onReadyForSpeech(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onBeginningOfSpeech() {
//
//            }
//
//            @Override
//            public void onRmsChanged(float v) {
//
//            }
//
//            @Override
//            public void onBufferReceived(byte[] bytes) {
//
//            }
//
//            @Override
//            public void onEndOfSpeech() {
//
//            }
//
//            @Override
//            public void onError(int i) {
////                if(i == SpeechRecognizer.ERROR_SPEECH_TIMEOUT){
////                    listenForAnswer(question);
////                }
//                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG);
//            }
//
//            @Override
//            public void onResults(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onPartialResults(Bundle bundle) {
//
//            }
//
//            @Override
//            public void onEvent(int i, Bundle bundle) {
//
//            }
//        };
//    }


    public void setDescription(long id, TextView textView){
        String desc = MainActivity.bViewModel.getDesc(id);
        textView.setText(desc);
    }

    public void setTimeDueText(long id, TextView textView){
        String fullTimeDue = getInitialTimeDue(id);
        fullTimeDue = fullTimeDue.substring(fullTimeDue.length() - 5);
        textView.setText(fullTimeDue);
    }

    public String getInitialTimeDue(long id){
        return MainActivity.bViewModel.getInitialTimeDue(id); }

    public void setSnoozeCount(long id, TextView textView){
        int result = MainActivity.bViewModel.getSnoozeCount(id);
        textView.setText(Integer.toString(result));
    }


}
