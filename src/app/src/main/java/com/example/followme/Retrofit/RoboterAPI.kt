package com.example.followme.Retrofit

import com.example.followme.Entity.Roboter
import com.example.followme.Entity.Route
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface, was die Schnittstelle zur API bereitstellt
 */
interface RoboterAPI {
    // das ist das REST-Interface in der App

    /**
     * GET-Methode um Roboter abzurufen
     * @return ArrayList<Roboter>
     */
    @GET("/roboter/getAll")
    fun getRobotList(): Call<ArrayList<Roboter>>

    /**
     * POST-Methode um Roboter abzuspeichern
     * @param roboter
     */
    @POST("/roboter/save")
    fun saveRoboter(@Body roboter: Roboter): Call<Void>

    /**
     * PATCH-Methode um Boolean "isFollowing" der Roboter zu aktualisieren
     * @param id
     * @param isFollowing
     */
    @PATCH("/roboter/update/{id}")
    fun updateIsFollowing(@Path("id") id: Int, @Query("isFollowing") isFollowing: Boolean): Call<Void>

    /**
     * DELETE-Methode um Roboter zu löschen
     * @param id
     */
    @DELETE("/roboter/delete/{id}")
    fun deleteRoboter(@Path("id") id: Int): Call<Void>


    /**
     * GET-Methode um Routen abzurufen
     * @return ArrayList<Route>
     */
    @GET("/route/getAll")
    fun getRouteList(): Call<ArrayList<Route>>

    /**
     * POST -Methode um Routen zu speichern
     * @param route
     */
    @POST("/route/save")
    fun saveRoute(@Body route: Route) : Call<Void>
}

