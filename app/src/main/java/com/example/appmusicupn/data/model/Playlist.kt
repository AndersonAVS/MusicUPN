package com.example.appmusicupn.data.model

data class Playlist(
    val id: String = "",
    val usuarioId: String = "",
    val nombre: String = "",
    val cancionesIds: List<String> = emptyList()
)
