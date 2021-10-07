package com.example.easychatwebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class MainActivity extends AppCompatActivity {
Button btn;
EditText domain, bot_id;
    EasyChat easyChat;
    Button openTestAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.open_bot_btn);
        domain = findViewById(R.id.domain_et);
        bot_id = findViewById(R.id.bot_id_et);
        openTestAct = findViewById(R.id.open_test_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                easyChat = new EasyChat(MainActivity.this);
                easyChat.showBot(bot_id.getText().toString(), domain.getText().toString());
            }
        });

        openTestAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}