package com.example.week6.util;

import com.example.week6.model.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {

    private Retrofit retrofit;
    private ApiService service;
    private String token;

    private NetworkHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://conduit.productionready.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }

    // null로 선언해놓고
    static private NetworkHelper instance = null;

    // 한번이라도 불러오면 새로 생성하고, 기존 것은 그냥 쓰면 됨
    static public NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApiService getService() {
        return service;
    }
}
