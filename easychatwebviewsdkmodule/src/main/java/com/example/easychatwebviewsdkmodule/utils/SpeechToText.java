package com.example.easychatwebviewsdkmodule.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToText {
    private static final String TAG = "SpeechToText";
    private Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String editTextMessage;
    private  WebView webView;

    public SpeechToText(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    public void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        setSpeechRecognizerListener();
    }

    public void setSpeechRecognizerListener() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                Log.d(TAG, "onReadyForSpeech: ");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech: ");
                showToastMessage("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                Log.d(TAG, "onError() called with: i = [" + i + "]");
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:deactivate_mic_android();");
                    }
                });
                showToastMessage("Please speak clearly.");
            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d(TAG, "onResults() called with: bundle = [" + bundle + "]");
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String value = data.get(0);
                String operation ="";
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:speech_intent_for_android('"+value+"');");
                    }
                });
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void showToastMessage(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public SpeechRecognizer getSpeechRecognizer() {
        return speechRecognizer;
    }

    public Intent getSpeechRecognizerIntent() {
        return speechRecognizerIntent;
    }

}
