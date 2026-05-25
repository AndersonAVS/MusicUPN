package com.example.appmusicupn.data.model

data class UsuarioPerfil(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val fotoPerfil: String = "",
    val fechaRegistro: Long = 0L,
    val rol: String = "usuario"
)
