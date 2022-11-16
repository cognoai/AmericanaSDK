package com.example.easychatwebviewsdkmodule.rest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    public static <S> S createService(Class<S> serviceClass,String remoteUrl) {
        OkHttpClient client = null;
        client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(remoteUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(serviceClass);
    }

}
