package com.example.easychatwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class TestActivity extends AppCompatActivity {

    Button openWebView;
    EasyChat easyChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        openWebView = findViewById(R.id.open_web_view);

        easyChat = new EasyChat(this);

        openWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyChat.showBot("318","https://easychat-dev.allincall.in");
            }
        });
    }
}