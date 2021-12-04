package com.frances.badger;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SpeechRecognizerActivity extends AppCompatActivity {
    SpeechRecognizer speechRecognizer;
    RecognitionListener speechListener;
    String speech;
    TextView textView;
    Intent replyIntent;
    Intent intentI;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_recognizer);


        textView = findViewById(R.id.question_asked);

        Intent intent = getIntent();
        speech = intent.getStringExtra("Question");
        textView.setText(speech);

        replyIntent = new Intent();

        speechListener = getListener(speech);
        intentI = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentI.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentI.putExtra(RecognizerIntent.EXTRA_PROMPT, speech);
        intentI.putExtra("Question", speech);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(speechListener);
        speechRecognizer.startListening(intentI);

    }

    public RecognitionListener getListener(String question){
        return new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            Log.i("Speech", "Ready for speech");
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.i("Speech", "End of speech");
            }

            @Override
            public void onError(int i) {
                Log.i("Speech", "Error" + i);
                replyIntent.putExtra("Speech", "ERROR");
                setResult(RESULT_OK, replyIntent);
                finish();
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.i("Speech", "Received results");
                List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                replyIntent.putExtra("Speech", results.get(0));
                setResult(RESULT_OK, replyIntent);
                finish();
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Toast.makeText(getApplicationContext(), "Partial Results", Toast.LENGTH_LONG);

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        };
    }


}
