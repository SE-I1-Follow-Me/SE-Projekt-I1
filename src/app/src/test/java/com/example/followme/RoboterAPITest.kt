package com.example.followme

import com.example.followme.Entity.Roboter
import com.example.followme.Retrofit.RoboterAPI
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RoboterAPITest {

    private lateinit var roboterAPI: RoboterAPI
    private val baseUrl = "http://iseproject07e.informatik.htw-dresden.de:8080"

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        roboterAPI = retrofit.create(RoboterAPI::class.java)
    }

    @Test
    fun `getRobotList gets Response`() {

        val actualRobotsResponse = roboterAPI.getRobotList().execute()

        //Assert
        assertTrue(actualRobotsResponse.isSuccessful)
        val actualRobots = actualRobotsResponse.body()
        assertNotNull(actualRobots)
    }
}
