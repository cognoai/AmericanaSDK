
package com.example.easychatwebviewsdkmodule.modal.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AccessTokenResponse {

    @SerializedName("status")
    @Expose
    private int status;



    /**
     * No args constructor for use in serialization
     */
    public AccessTokenResponse() {
    }

    public AccessTokenResponse(int status) {
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
        return "AccessTokenResponse{" +
                "status=" + status +
                '}';
    }
}
