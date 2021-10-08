package com.example.easychatwebviewsdkmodule.rest;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.modal.Request.AccessTokenRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Response.AccessTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface EasyChatApi {
    String BASE_URL = GlobalParams.base_url;

    @POST("/chat/verify-access-token/")
    Call<AccessTokenResponse> verifyAccessToken(@Header("Content-Type") String contentType,
                                                @Body AccessTokenRequestPacket accessTokenRequestPacket);




}
