package com.example.easychatwebview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class MainActivity extends AppCompatActivity {
    private EditText domain, bot_id, token_et, mobile_session_id_et, language_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.open_bot_btn);
        domain = findViewById(R.id.domain_et);
        bot_id = findViewById(R.id.bot_id_et);
        Button openTestAct = findViewById(R.id.open_test_activity);
        token_et = findViewById(R.id.access_token_et);
        Button verify_access_token = findViewById(R.id.verify_access_token);
        mobile_session_id_et = findViewById(R.id.mob_session_id_et);
        language_et = findViewById(R.id.language_et);

        EasyChat.setTheme("Automatic");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String custom_params = "{\"mobile_session_id\":\"" + mobile_session_id_et.getText().toString().trim() + "\"}";
                EasyChat.setCustomParameters(custom_params);
                EasyChat.showBot(getSupportFragmentManager(), (FragmentActivity) MainActivity.this);
            }
        });

        openTestAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        verify_access_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyChat.setBot_id(bot_id.getText().toString());
                EasyChat.setBase_url(domain.getText().toString());
                EasyChat.setAccess_token(token_et.getText().toString());
                EasyChat.setSelectedLanguage(language_et.getText().toString());

                EasyChat.verifyAccessToken((FragmentActivity) MainActivity.this);
                if (EasyChat.isAccessTokenVerified()) {
                    Toast.makeText(MainActivity.this, "Access token verified", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}