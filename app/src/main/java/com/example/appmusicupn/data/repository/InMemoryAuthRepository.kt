package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Usuario

class InMemoryAuthRepository : AuthRepository {
    private val usuarios = mutableMapOf<String, Usuario>()

    private val passwords = mutableMapOf<String, String>()

    override suspend fun login(correo: String, password: String): RepositoryResult<Usuario> {
        val key = correo.trim()
        val usuario = usuarios[key]
        val savedPassword = passwords[key]

        return if (usuario != null && savedPassword == password) {
            RepositoryResult.Success(usuario)
        } else {
            RepositoryResult.Error("Credenciales incorrectas")
        }
    }

    override suspend fun registrar(
        nombre: String,
        correo: String,
        password: String
    ): RepositoryResult<Usuario> {
        val key = correo.trim()

        if (usuarios.containsKey(key)) {
            return RepositoryResult.Error("El correo ya está registrado")
        }

        val usuario = Usuario(
            id = "user-${usuarios.size + 1}",
            nombre = nombre.trim(),
            correo = key
        )

        usuarios[key] = usuario
        passwords[key] = password

        return RepositoryResult.Success(usuario)
    }

    override suspend fun restablecerPassword(correo: String): RepositoryResult<Unit> {
        val key = correo.trim()

        return if (usuarios.containsKey(key)) {
            RepositoryResult.Success(Unit)
        } else {
            RepositoryResult.Error("No encontramos una cuenta con ese correo")
        }
    }
    override fun cerrarSesion() {
        // No hay sesión real que cerrar en memoria
    }
}
