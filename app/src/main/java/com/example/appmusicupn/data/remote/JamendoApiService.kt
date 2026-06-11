package com.example.appmusicupn.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApiService {
    @GET("tracks")
    suspend fun buscarTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 20,
        @Query("search") search: String,
        @Query("audioformat") audioFormat: String = "mp32"
    ): JamendoResponse
}
