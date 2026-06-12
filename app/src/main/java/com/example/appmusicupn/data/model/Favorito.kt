package com.example.appmusicupn.data.model

data class Favorito(
    val id: String = "",
    val titulo: String = "",
    val artista: String = "",
    val album: String = "",
    val audioUrl: String = "",
    val portadaUrl: String = "",
    val origen: String = "",
    val fechaAgregado: Long = 0L
)
