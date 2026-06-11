package com.example.appmusicupn.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JamendoApiClient {
    val service: JamendoApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jamendo.com/v3.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JamendoApiService::class.java)
    }
}
