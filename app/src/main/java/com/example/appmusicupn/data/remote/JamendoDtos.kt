package com.example.appmusicupn.data.remote

data class JamendoResponse(
    val results: List<JamendoTrackDto> = emptyList()
)

data class JamendoTrackDto(
    val id: String = "",
    val name: String = "",
    val artist_name: String = "",
    val album_name: String = "",
    val audio: String = "",
    val album_image: String = ""
)
