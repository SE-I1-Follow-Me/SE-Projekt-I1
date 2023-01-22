package com.example.followme.Retrofit

import com.example.followme.Entity.Roboter
import retrofit2.http.GET
import java.util.ArrayList
import retrofit2.Call
import retrofit2.http.POST

interface RoboterAPI {

    @GET("/roboter/get-all")
    fun getRobotList(): Call<ArrayList<Roboter>>
}

