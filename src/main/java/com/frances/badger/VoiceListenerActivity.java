package com.frances.badger;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceListenerActivity extends AppCompatActivity {
    final String request = "request";
    String requestReceived;
    Intent replyIntent;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_recognizer);

        Intent received = getIntent();
        requestReceived = received.getStringExtra(request);
        textView = findViewById(R.id.question_asked);
        textView.setText(requestReceived);


        replyIntent = new Intent();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(requestReceived.equals("hour")){
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What hour?");
            startActivityForResult(intent, 101);
        }
        if(requestReceived.equals("minute")){
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What minute?");
            startActivityForResult(intent, 202);
        }
        if(requestReceived.equals("snooze")){
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What snooze interval?");
            startActivityForResult(intent, 303);
        }
        if(requestReceived.equals("date")){
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Which day?");
            startActivityForResult(intent, 404);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                replyIntent.putExtra("Speech Received", results.get(0));
                replyIntent.putExtra("Code Received", requestCode);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
            if(resultCode == RESULT_CANCELED){
                replyIntent.putExtra("Code Received", 0); //0 is the failure code
                setResult(RESULT_OK, replyIntent);
                finish();
            }

    }


}
