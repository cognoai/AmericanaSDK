package com.example.easychatwebviewsdkmodule.modal.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenRequestPacket {
    @SerializedName("access_token")
    @Expose
    private String access_token;

    @SerializedName("bot_id")
    @Expose
    private String bot_id;

    public AccessTokenRequestPacket(String access_token, String bot_id) {
        this.access_token = access_token;
        this.bot_id = bot_id;
    }

    public AccessTokenRequestPacket() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getBot_id() {
        return bot_id;
    }

    public void setBot_id(String bot_id) {
        this.bot_id = bot_id;
    }

    @Override
    public String toString() {
        return "AccessTokenRequestPacket{" +
                "access_token='" + access_token + '\'' +
                ", bot_id='" + bot_id + '\'' +
                '}';
    }
}
