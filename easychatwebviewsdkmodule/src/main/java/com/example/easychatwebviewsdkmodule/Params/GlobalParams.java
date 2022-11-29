package com.example.easychatwebviewsdkmodule.Params;

import android.util.Log;

import com.example.easychatwebviewsdkmodule.dialog.ChatDialog;

public class GlobalParams {
    public static String bot_id;
    public static String domain_name;
    public static boolean bot_minimized = false;
    public static ChatDialog chatDialog;
    public static String base_url;
    public static String access_token = "";
    public static String categoryName = "";
    public static boolean access_token_verified = false;
    private static String theme = "Light";

    private static String finalUrl = "";
    private static String mobileSessionId = "";
    private static boolean storeChatPermanently = false;
    private static String random_uuid = "";
    private static String selected_language = "";
    private static String liveChatSessionId = "";

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

    public static String getCategoryName() {
        return categoryName.trim();
    }

    public static void setCategoryName(String categoryName) {
        GlobalParams.categoryName = categoryName;
    }

    public static String getFinalUrl() {
        return finalUrl;
    }

    public static void setFinalUrl(String finalUrl) {
        GlobalParams.finalUrl = finalUrl;
    }

    public static String getMobileSessionId() {
        return mobileSessionId;
    }

    public static void setMobileSessionId(String mobileSessionId) {
        GlobalParams.mobileSessionId = mobileSessionId;
    }

    public static boolean isStoreChatPermanently() {
        return storeChatPermanently;
    }

    public static void setStoreChatPermanently(boolean storeChatPermanently) {
        GlobalParams.storeChatPermanently = storeChatPermanently;
    }

    public static String getRandom_uuid() {
        return random_uuid;
    }

    public static void setRandom_uuid(String random_uuid) {
        GlobalParams.random_uuid = random_uuid;
    }

    public static String getSelected_language() {
        return selected_language;
    }

    public static void setSelected_language(String selected_language) {
        GlobalParams.selected_language = selected_language;
    }

    public static String getLiveChatSessionId() {
        return liveChatSessionId;
    }

    public static void setLiveChatSessionId(String liveChatSessionId) {
        GlobalParams.liveChatSessionId = liveChatSessionId;
    }
}
