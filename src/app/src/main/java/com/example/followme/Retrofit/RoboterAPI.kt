package com.example.followme.Retrofit

import com.example.followme.Entity.Roboter
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RoboterAPI {
    // das ist das REST-Interface in der App

    //Die GET-Methode, ist Sachen vom Server rufen, andere folgen im Laufe des Projekts
    @GET("/roboter/getAll")
    fun getRobotList(): Call<ArrayList<Roboter>>

    @POST("/roboter/save")
    fun saveRoboter(@Body roboter: Roboter): Call<Void>






}

