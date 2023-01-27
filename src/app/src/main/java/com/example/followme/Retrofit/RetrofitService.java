package com.example.followme.Retrofit;

import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder();
                .baseUrl("http://iseproject07e.informatik.htw-dresden.de:3306")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
}
