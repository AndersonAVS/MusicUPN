package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.UserRole
import com.example.appmusicupn.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.example.appmusicupn.data.model.UsuarioPerfil
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun login(
        correo: String,
        password: String
    ): RepositoryResult<Usuario> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(correo.trim(), password)//inicia sesion
                .await()//permite usar firebase como corrutinas

            val firebaseUser = result.user
                ?: return RepositoryResult.Error("No se pudo obtener el usuario")

            val usuario = Usuario(
                id = firebaseUser.uid,
                nombre = firebaseUser.displayName ?: "Usuario",
                correo = firebaseUser.email ?: correo.trim(),
                rol = UserRole.USER
            )

            RepositoryResult.Success(usuario)
        } catch (exception: Exception) {
            val mensaje = when (exception) {
                is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos"
                is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                else -> "No se pudo iniciar sesión. Intenta nuevamente"
            }

            RepositoryResult.Error(mensaje)
        }
    }

    override suspend fun registrar(
        nombre: String,
        correo: String,
        password: String
    ): RepositoryResult<Usuario> {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(correo.trim(), password)//registra un usuario
                .await()//permite usar firebase como corrutinas

            val firebaseUser = result.user
                ?: return RepositoryResult.Error("No se pudo crear el usuario")

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nombre.trim())
                .build()

            firebaseUser.updateProfile(profileUpdates).await()//guarda el nombre en el perfil basico de firebase auth

            val perfil = UsuarioPerfil(
                uid = firebaseUser.uid,
                nombre = nombre.trim(),
                correo = firebaseUser.email ?: correo.trim(),
                fotoPerfil = "",
                fechaRegistro = System.currentTimeMillis(),
                rol = "usuario"
            )

            firestore
                .collection("usuarios")
                .document(firebaseUser.uid)
                .set(perfil)
                .await()

            val usuario = Usuario(
                id = firebaseUser.uid,
                nombre = nombre.trim(),
                correo = firebaseUser.email ?: correo.trim(),
                rol = UserRole.USER
            )

            RepositoryResult.Success(usuario)
        } catch (exception: Exception) {
            RepositoryResult.Error( "No se pudo registrar el usuario"
            )
        }
    }

    override suspend fun restablecerPassword(correo: String): RepositoryResult<Unit> {
        val correoLimpio = correo.trim()

        if (correoLimpio.isBlank()) {
            return RepositoryResult.Error("Ingresa tu correo para restablecer tu contraseña")
        }

        return try {
            firebaseAuth
                .sendPasswordResetEmail(correoLimpio)
                .await()

            RepositoryResult.Success(Unit)
        } catch (exception: Exception) {
            RepositoryResult.Error("No pudimos enviar el correo de recuperación")
        }
    }
    override fun cerrarSesion() {
        firebaseAuth.signOut()
    }

}
