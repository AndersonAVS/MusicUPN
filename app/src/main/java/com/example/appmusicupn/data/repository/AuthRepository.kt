package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Usuario

interface AuthRepository {
    fun login(correo: String, password: String): RepositoryResult<Usuario>

    fun registrar(
        nombre: String,
        correo: String,
        password: String
    ): RepositoryResult<Usuario>
}
