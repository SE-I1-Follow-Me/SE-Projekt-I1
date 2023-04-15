package com.example.followme.Retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//Für die Konvertierung der Daten von JSON zu Arrays
class RetrofitService {
    private lateinit var retrofit: Retrofit

    init {
        initializeRetrofit()
    }

    private fun initializeRetrofit() {
        retrofit = Retrofit.Builder()
                //Url wird geadded, wo es konvertiert werden soll, HTW-Server-Port
            .baseUrl("http://iseproject07e.informatik.htw-dresden.de:3306")
                //JSon Libary von Google wird benutzt G-Son, witziger Name Hahaha
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }
}