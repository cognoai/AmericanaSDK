package com.example.easychatwebview;

import android.app.Application;
import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Bot 1
//        EasyChat.setBot_id("3");
//        EasyChat.setBase_url("https://americana-uae.allincall.in");
//        EasyChat.setAccess_token("0ef384d3-fd8a-4833-88c7-245c351c4dc0");
//        EasyChat.setCustomParameters("{\"mobile_session_id\":\"548688878\"}");
//        EasyChat.setSelectedLanguage("en");

//        Bot 2
        EasyChat.setBot_id("2");
        EasyChat.setBase_url("https://americana-uae.allincall.in");
        EasyChat.setAccess_token("6239e892-05ad-49b4-abc7-a3b0bb303309");
        EasyChat.setCustomParameters("{\"mobile_session_id\":\"561310981\"}");
        EasyChat.setSelectedLanguage("en");
    }
}
