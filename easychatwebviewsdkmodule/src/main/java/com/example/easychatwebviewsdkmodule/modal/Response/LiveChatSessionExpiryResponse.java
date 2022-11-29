package com.example.easychatwebviewsdkmodule.modal.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveChatSessionExpiryResponse {

    @SerializedName("status")
    @Expose
    private int status;

    /**
     * No args constructor for use in serialization
     */
    public LiveChatSessionExpiryResponse() {
    }

    public LiveChatSessionExpiryResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LiveChatSessionExpiryResponse{" +
                "status=" + status +
                '}';
    }
}

