package com.example.easychatwebviewsdkmodule.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TextToSpeech {
    private static final String TAG = "TextToSpeech";
    private Context context;
    private android.speech.tts.TextToSpeech textToSpeech;
    public TextToSpeech(Context context) {
        this.context = context;
    }

    public void initialiseTextToSpeech() {
        textToSpeech = new android.speech.tts.TextToSpeech(context, new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.UK);
                    installLanguageCode(result);
                } else {
                    Log.e(TAG, "onInit: initialization failed with error code = " + status);
                }
                Log.d(TAG, "onInit: ");
            }
        });
    }

    public void installLanguageCode(int result) {
        if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA ||
                result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TAG, "onInit: Language not supported!");

            // code which leads to installation of language specific resource files
            Intent installIntent = new Intent();
            installIntent.setAction(android.speech.tts.TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            context.startActivity(installIntent);
        }
    }

    public void convertTextToSpeech(String message, String languageCode) {
        if (!message.isEmpty()) {
            int result = textToSpeech.setLanguage(Locale.forLanguageTag(languageCode));
//            installLanguageCode(result);
            textToSpeech.speak(message, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, "EasyChat");

        }
    }

    public void shutdownTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }

    public void terminateTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public android.speech.tts.TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }
}
