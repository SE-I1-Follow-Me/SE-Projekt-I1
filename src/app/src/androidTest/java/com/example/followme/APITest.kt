package com.example.followme

import com.example.followme.Retrofit.RoboterAPI
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RoboterAPITest {

    private lateinit var server: MockWebServer
    private lateinit var api: RoboterAPI

    @Before
    fun setUp() {
        // Start the mock server.
        server = MockWebServer()
        server.start()

        // Set up the API to use the mock server.
        api = Retrofit.Builder()
            .baseUrl(server.url("/")) // Tell the API to use the mock server.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RoboterAPI::class.java)
    }

    @Test
    fun testGetRobotList() {
        // Prepare the mock response.
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[{\"id\":1, \"name\":\"Robot 1\", \"isFollowing\": false}, {\"id\":2, \"name\":\"Robot 2\", \"isFollowing\": true}]"))

        // Call the API.
        val response = api.getRobotList().execute()

        // Assert that the response is successful and contains the correct data.
        assert(response.isSuccessful)
        val robots = response.body()
        assertEquals(2, robots?.size)
        assertEquals("Robot 1", robots?.get(0)?.getName())
        assertEquals(false, robots?.get(0)?.getIsFollowing())
        assertEquals("Robot 2", robots?.get(1)?.getName())
        assertEquals(true, robots?.get(1)?.getIsFollowing())
    }


    @After
    fun tearDown() {
        // Shut down the server. Always make sure to shut down the server, even if something fails.
        server.shutdown()
    }
}


