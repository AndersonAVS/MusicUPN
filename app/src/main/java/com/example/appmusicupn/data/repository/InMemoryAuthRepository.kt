package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.UserRole
import com.example.appmusicupn.data.model.Usuario

class InMemoryAuthRepository : AuthRepository {
    private val usuarios = mutableMapOf(
        "admin@musicupn.edu.pe" to Usuario(
            id = "admin",
            nombre = "Administrador",
            correo = "admin@musicupn.edu.pe",
            rol = UserRole.ADMIN
        ),
        "admin" to Usuario(
            id = "admin-local",
            nombre = "Administrador",
            correo = "admin",
            rol = UserRole.ADMIN
        )
    )

    private val passwords = mutableMapOf(
        "admin@musicupn.edu.pe" to "admin123",
        "admin" to "1234"
    )

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
            correo = key,
            rol = UserRole.USER
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
