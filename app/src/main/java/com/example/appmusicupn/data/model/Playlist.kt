package com.example.appmusicupn.data.model

data class Playlist(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val fechaCreacion: Long = 0L,
    val cancionesIds: List<String> = emptyList()
)
