package com.example.appmusicupn.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val rol: UserRole = UserRole.USER
)
