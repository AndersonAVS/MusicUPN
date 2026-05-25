package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Usuario

interface AuthRepository {
    suspend fun login(correo: String, password: String): RepositoryResult<Usuario>

    suspend fun registrar(
        nombre: String,
        correo: String,
        password: String
    ): RepositoryResult<Usuario>

    suspend fun restablecerPassword(correo: String): RepositoryResult<Unit>
}
