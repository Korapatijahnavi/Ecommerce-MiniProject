package com.example.ecommerceminiproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static ApiService apiservice;
    private static Retrofit retrofit;
    private RetrofitClient(){
    }
    public static ApiService getApiservice() {
        if (apiservice == null) {
            retrofit=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiservice=retrofit.create(ApiService.class);
        }
        return apiservice;
    }
}
