package com.example.easychatwebview;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        EasyChat.setBot_id("2");
        EasyChat.setBase_url("https://8175-59-89-34-92.in.ngrok.io");
        EasyChat.setAccess_token("8fa64504-4ea1-44d0-b285-4157463d2b6d");
        EasyChat.setCustomParameters("{\"name\":\"John\",\"city\":\"New York\"}");
    }
}
