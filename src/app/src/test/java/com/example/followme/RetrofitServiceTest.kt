package com.example.followme

import com.example.followme.Retrofit.RetrofitService
import org.junit.Test

import org.junit.Assert.*
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceTest {

    private val retrofitService = RetrofitService()

    @Test
    fun `retrofit is initialized correctly`() {
        val retrofit = retrofitService.getRetrofit()

        assertEquals("http://iseproject07e.informatik.htw-dresden.de:8080/", retrofit.baseUrl().toString())
        assertTrue(retrofit.converterFactories().any { it is GsonConverterFactory })
    }
}
