package com.example.easychatwebviewsdkmodule.ViewModal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.easychatwebviewsdkmodule.modal.Request.AccessTokenRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Response.AccessTokenResponse;
import com.example.easychatwebviewsdkmodule.rest.MainRepository;

public class AuthenticationViewModal extends ViewModel {

    private MainRepository mainRepository;

    public void init() {
        mainRepository = MainRepository.getInstance();
        mainRepository.setEasyChatApi();
    }

    public LiveData<AccessTokenResponse> verifyAccessToken(AccessTokenRequestPacket accessTokenRequestPacket) {
        return mainRepository.verifyAccessToken(accessTokenRequestPacket);
    }

    public void resetAccessTokenResponseMutableLiveData() {
        mainRepository.getAcessTokenMutableLiveData().setValue(null);
    }
}
