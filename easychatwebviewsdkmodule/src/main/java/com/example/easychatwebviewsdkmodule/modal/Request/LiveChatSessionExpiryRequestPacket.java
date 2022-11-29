package com.example.easychatwebviewsdkmodule.modal.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveChatSessionExpiryRequestPacket {
    @SerializedName("livechat_session_id")
    @Expose
    private String livechat_session_id;

    public LiveChatSessionExpiryRequestPacket(String livechat_session_id) {
        this.livechat_session_id = livechat_session_id;
    }

    public LiveChatSessionExpiryRequestPacket() {
    }

    public String getLivechat_session_id() {
        return livechat_session_id;
    }

    public void setLivechat_session_id(String livechat_session_id) {
        this.livechat_session_id = livechat_session_id;
    }

    @Override
    public String toString() {
        return "LiveChatSessionExpiryRequestPacket{" +
                "livechat_session_id='" + livechat_session_id +
                '}';
    }
}
