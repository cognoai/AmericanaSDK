package com.example.easychatwebviewsdkmodule.rest;

import java.time.Duration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    public static <S> S createService(Class<S> serviceClass,String remoteUrl) {
        OkHttpClient client = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofSeconds(120))
                    .writeTimeout(Duration.ofSeconds(30))
                    .readTimeout(Duration.ofSeconds(30))
                    .build();
        }
        return new Retrofit.Builder()
                .baseUrl(remoteUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(serviceClass);
    }

}
