package com.example.easychatwebviewsdkmodule.sdkclasses;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.dialog.ChatDialog;

public class EasyChat {

    static Activity activity;
    private ChatDialog cd;

    public EasyChat(Activity activity){
        this.activity=activity;
    }
    public void showBot(String bot_id, String domain) {
        Log.i("TAG", "showBot: "+GlobalParams.isBot_minimized());
        if (GlobalParams.isBot_minimized()) {
            GlobalParams.setBot_minimized(false);
            if(GlobalParams.getChatDialog() != null)
            {
                if(GlobalParams.getChatDialog().getDialog()!=null)
                {
                    GlobalParams.getChatDialog().getDialog().show();
                } else {
                    GlobalParams.bot_id = bot_id;
                    GlobalParams.domain_name = domain;
                    ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
                    GlobalParams.setChatDialog(cd);
                    GlobalParams.setBot_minimized(false);
                    showDialog(cd, "chats");
                }
            } else {

                GlobalParams.bot_id = bot_id;
                GlobalParams.domain_name = domain;
                ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
                GlobalParams.setChatDialog(cd);
                GlobalParams.setBot_minimized(false);
                showDialog(cd, "chats");
            }

        } else
        {
            GlobalParams.bot_id = bot_id;
            GlobalParams.domain_name = domain;
            ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
            GlobalParams.setChatDialog(cd);
            GlobalParams.setBot_minimized(false);
            showDialog(cd, "chats");

        }
    }

    public DialogFragment get_chat_dialog()
    {
        return cd;
    }

    private static void showDialog(DialogFragment dialogFragment, String tag) {

        FragmentTransaction ft = ((FragmentActivity) activity).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null && !tag.equals("endSession")) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(((AppCompatActivity) activity).getSupportFragmentManager(), tag);
    }
}