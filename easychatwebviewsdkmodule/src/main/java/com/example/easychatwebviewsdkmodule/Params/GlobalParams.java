package com.example.easychatwebviewsdkmodule.Params;

import com.example.easychatwebviewsdkmodule.dialog.ChatDialog;

public class GlobalParams {
    public static String bot_id;
    public static String domain_name;
    public static boolean bot_minimized = false;
    public static ChatDialog chatDialog;
    public static String base_url;
    public static String access_token = "";
    public static boolean access_token_verified = false;
    private static String theme = "Light";

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String theme) {
        GlobalParams.theme = theme;
    }

    public static String getAccess_token() {
        return access_token;
    }

    public static void setAccess_token(String access_token) {
        GlobalParams.access_token = access_token;
    }

    public static String getBase_url() {
        return base_url;
    }

    public static void setBase_url(String base_url) {
        GlobalParams.base_url = base_url;
    }

    public static boolean isBot_minimized() {
        return bot_minimized;
    }

    public static void setBot_minimized(boolean bot_minimized) {
        GlobalParams.bot_minimized = bot_minimized;
    }

    public static String getBot_id() {
        return bot_id;
    }

    public static void setBot_id(String bot_id) {
        GlobalParams.bot_id = bot_id;
    }

    public static String getDomain_name() {
        return domain_name;
    }

    public static void setDomain_name(String domain_name) {
        GlobalParams.domain_name = domain_name;
    }

    public static ChatDialog getChatDialog() {
        return chatDialog;
    }

    public static void setChatDialog(ChatDialog chatDialog) {
        GlobalParams.chatDialog = chatDialog;
    }
}
