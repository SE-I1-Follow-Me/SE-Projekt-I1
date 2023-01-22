package com.example.followme.Retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    private lateinit var retrofit: Retrofit

    init {
        initializeRetrofit()
    }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://iseproject07e.informatik.htw-dresden.de:3306")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }
}