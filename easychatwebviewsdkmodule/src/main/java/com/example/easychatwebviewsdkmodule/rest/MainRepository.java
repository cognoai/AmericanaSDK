package com.example.easychatwebviewsdkmodule.rest;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.easychatwebviewsdkmodule.Params.GlobalParams;
import com.example.easychatwebviewsdkmodule.modal.Request.AccessTokenRequestPacket;
import com.example.easychatwebviewsdkmodule.modal.Response.AccessTokenResponse;
import com.example.easychatwebviewsdkmodule.utils.CryptoUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private static final String TAG = "MainRepository";

    private static MainRepository instance;
    private EasyChatApi easyChatApi;
    private Gson gson;
    MutableLiveData<AccessTokenResponse> accessTokenMutableLiveData = new MutableLiveData<>();

    private CompositeDisposable disposables = new CompositeDisposable();

    private MainRepository() {
        gson = new Gson();
    }

    public void setEasyChatApi() {
      String remoteUrl = GlobalParams.getBase_url();
//      String remoteUrl = "https://75b6-117-220-140-126.in.ngrok.io"; // Shrrayan ngrok
     //  String remoteUrl = "https://4e55364569fc.ngrok.io";  //Udit
   //       String remoteUrl = "https://08b277cc9ae3.ngrok.io";  //Nayan


        easyChatApi = RetrofitService.createService(EasyChatApi.class, remoteUrl);
    }

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public MutableLiveData<AccessTokenResponse> verifyAccessToken(AccessTokenRequestPacket accessTokenRequestPacket) {
        Log.d(TAG, "verifyAccessToken() called with: accessTokenRequestPacket = [" + accessTokenRequestPacket.toString() + "]");
        Call<AccessTokenResponse> call = easyChatApi.verifyAccessToken("application/json", accessTokenRequestPacket);
        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                Log.d(TAG, "onResponse: before success" + response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                            AccessTokenResponse accessTokenResponse = response.body();
                            Log.d(TAG, "onResponse: " + accessTokenResponse.toString());
                            accessTokenMutableLiveData.setValue(accessTokenResponse);
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response);
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failed " + t.getMessage());

            }
        });

        return accessTokenMutableLiveData;
    }


    public MutableLiveData<AccessTokenResponse> getAcessTokenMutableLiveData() {
        return accessTokenMutableLiveData;
    }



}


