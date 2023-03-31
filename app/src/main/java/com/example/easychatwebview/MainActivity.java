package com.example.easychatwebview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.sdkclasses.EasyChat;

public class MainActivity extends AppCompatActivity {
Button btn;
EditText domain, bot_id, token_et, category_name_et, final_url_et;
    Button openTestAct, verify_access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.open_bot_btn);
        domain = findViewById(R.id.domain_et);
        bot_id = findViewById(R.id.bot_id_et);
        openTestAct = findViewById(R.id.open_test_activity);
        token_et = findViewById(R.id.access_token_et);
        verify_access_token = findViewById(R.id.verify_access_token);
        category_name_et = findViewById(R.id.category_name_et);
        final_url_et = findViewById(R.id.final_url_et);

        EasyChat.setTheme("Automatic");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyChat.setCategoryName(category_name_et.getText().toString());
                if (final_url_et.getText().toString().trim().isEmpty()) {
                    EasyChat.setFinalUrl("");
                } else {
                    EasyChat.setFinalUrl(final_url_et.getText().toString().trim());
                }
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
                EasyChat.setSelectedLanguage("en");

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