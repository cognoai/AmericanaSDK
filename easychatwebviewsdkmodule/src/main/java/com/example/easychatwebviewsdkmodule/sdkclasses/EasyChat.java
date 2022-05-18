package com.example.easychatwebviewsdkmodule.sdkclasses;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.ViewModal.AuthenticationViewModal;
import com.example.easychatwebviewsdkmodule.dialog.ChatDialog;
import com.example.easychatwebviewsdkmodule.modal.Request.AccessTokenRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Response.AccessTokenResponse;

public class EasyChat {

    private ChatDialog cd;
    private static AuthenticationViewModal authenticationViewModal;

    public static void showBot(FragmentManager fragmentManager, FragmentActivity fragmentActivity) {
        if( GlobalParams.access_token_verified) {
            Log.i("TAG", "showBot: " + GlobalParams.isBot_minimized());
            if (GlobalParams.isBot_minimized()) {
                GlobalParams.setBot_minimized(false);
                if (GlobalParams.getChatDialog() != null) {
                    if (GlobalParams.getChatDialog().getDialog() != null) {
                        GlobalParams.getChatDialog().getDialog().show();
                    } else {
                        ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
                        GlobalParams.setChatDialog(cd);
                        GlobalParams.setBot_minimized(false);
//                        showDialog(cd, "chats");
                        cd.show(fragmentManager, "chats");
                    }
                } else {

                    ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
                    GlobalParams.setChatDialog(cd);
                    GlobalParams.setBot_minimized(false);
//                    showDialog(cd, "chats");
                    cd.show(fragmentManager, "chats");
                }

            } else {

                ChatDialog cd = (ChatDialog) ChatDialog.newInstance("ChatDialog");
                GlobalParams.setChatDialog(cd);
                GlobalParams.setBot_minimized(false);
//                showDialog(cd, "chats");

                cd.show(fragmentManager, "chats");
            }
        } else {
            Toast.makeText(fragmentActivity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public DialogFragment get_chat_dialog()
    {
        return cd;
    }

    public static void verifyAccessToken(FragmentActivity fragmentActivity) {
            authenticationViewModal = ViewModelProviders.of(fragmentActivity).get(AuthenticationViewModal.class);
            authenticationViewModal.init();
            AccessTokenRequestPacket accessTokenRequestPacket = new AccessTokenRequestPacket(GlobalParams.getAccess_token().toString(), GlobalParams.getBot_id().toString());

            authenticationViewModal.verifyAccessToken(accessTokenRequestPacket).observe((LifecycleOwner) fragmentActivity, new Observer<AccessTokenResponse>() {
                @Override
                public void onChanged(AccessTokenResponse accessTokenResponse) {
                    if (accessTokenResponse != null) {
                        Log.d("accessTokenResponse", "onChanged() called with: accessTokenResponse = [" + accessTokenResponse.toString() + "]");
                        if (accessTokenResponse.getStatus() == 200) {
                            GlobalParams.access_token_verified = true;
                            Toast.makeText(fragmentActivity, "Access Token Verified", Toast.LENGTH_SHORT).show();

                        } else {
                            GlobalParams.access_token_verified = false;
                        }
                    }
                }
            });
    }

    public static void setAccess_token(String access_token) {
        GlobalParams.access_token = access_token;
    }

    public static void setCategoryName(String categoryName) {
        GlobalParams.setCategoryName(categoryName);
    }

    public static void setBase_url(String base_url) {
        GlobalParams.base_url = base_url;
    }

    public static void setBot_id(String bot_id) {
        GlobalParams.bot_id = bot_id;
    }

    public static void setTheme(String theme){
        GlobalParams.setTheme(theme);
    }
}