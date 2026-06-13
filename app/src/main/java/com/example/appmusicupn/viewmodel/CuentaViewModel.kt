package com.example.appmusicupn.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmusicupn.data.model.UsuarioPerfil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class CuentaUiState(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val correoVerificado: Boolean = false,
    val fotoPerfil: String = "",
    val fechaRegistro: Long = 0L,
    val rol: String = "usuario",
    val error: String = "",
    val mensaje: String = "",
    val guardandoDatos: Boolean = false,
    val subiendoFoto: Boolean = false,
    val mostrarDialogCorreo: Boolean = false,
    val mostrarDialogPassword: Boolean = false,
    val mostrarDialogEliminarCuenta: Boolean = false,
    val passwordActual: String = "",
    val nuevoCorreo: String = "",
    val nuevoPassword: String = "",
    val confirmarNuevoPassword: String = "",
    val actualizandoCorreo: Boolean = false,
    val actualizandoPassword: Boolean = false,
    val eliminandoCuenta: Boolean = false,
    val cuentaEliminada: Boolean = false
)

class CuentaViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {

    var uiState by mutableStateOf(CuentaUiState())
        private set

    init {
        cargarUsuario()
    }

    private fun cargarUsuario() {
        val usuario = firebaseAuth.currentUser

        if (usuario == null) {
            uiState = uiState.copy(error = "No hay una sesión activa")
            return
        }

        viewModelScope.launch {
            try {
                usuario.reload().await()

                val snapshot = firestore
                    .collection("usuarios")
                    .document(usuario.uid)
                    .get()
                    .await()

                val perfil = snapshot.toObject(UsuarioPerfil::class.java)
                val correoAuth = usuario.email.orEmpty()
                val correoPerfil = if (correoAuth.isNotBlank()) {
                    correoAuth
                } else {
                    perfil?.correo.orEmpty()
                }

                uiState = uiState.copy(
                    uid = usuario.uid,
                    nombre = perfil?.nombre ?: usuario.displayName.orEmpty(),
                    correo = correoPerfil,
                    correoVerificado = usuario.isEmailVerified,
                    fotoPerfil = perfil?.fotoPerfil ?: usuario.photoUrl?.toString().orEmpty(),
                    fechaRegistro = perfil?.fechaRegistro ?: 0L,
                    rol = perfil?.rol ?: "usuario"
                )

                if (correoAuth.isNotBlank() && perfil?.correo != correoAuth) {
                    firestore
                        .collection("usuarios")
                        .document(usuario.uid)
                        .set(mapOf("correo" to correoAuth), SetOptions.merge())
                        .await()
                }
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    uid = usuario.uid,
                    nombre = usuario.displayName.orEmpty(),
                    correo = usuario.email.orEmpty(),
                    correoVerificado = usuario.isEmailVerified,
                    error = "No se pudieron cargar los datos de la cuenta"
                )
            }
        }
    }

    fun onNombreChange(value: String) {
        uiState = uiState.copy(
            nombre = value,
            error = "",
            mensaje = ""
        )
    }

    fun guardarCambios() {
        val nombreLimpio = uiState.nombre.trim()

        if (nombreLimpio.isBlank()) {
            uiState = uiState.copy(
                error = "El nombre no puede estar vacío",
                mensaje = ""
            )
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                guardandoDatos = true,
                error = "",
                mensaje = ""
            )

            try {
                val usuario = firebaseAuth.currentUser
                    ?: throw IllegalStateException("No hay una sesión activa")

                val profileBuilder = UserProfileChangeRequest.Builder()
                    .setDisplayName(nombreLimpio)

                if (uiState.fotoPerfil.trim().isNotBlank()) {
                    profileBuilder.setPhotoUri(Uri.parse(uiState.fotoPerfil.trim()))
                }

                usuario.updateProfile(profileBuilder.build()).await()

                val perfilActualizado = UsuarioPerfil(
                    uid = usuario.uid,
                    nombre = nombreLimpio,
                    correo = usuario.email.orEmpty(),
                    fotoPerfil = uiState.fotoPerfil.trim(),
                    fechaRegistro = if (uiState.fechaRegistro == 0L) {
                        System.currentTimeMillis()
                    } else {
                        uiState.fechaRegistro
                    },
                    rol = uiState.rol.ifBlank { "usuario" }
                )

                firestore
                    .collection("usuarios")
                    .document(usuario.uid)
                    .set(perfilActualizado, SetOptions.merge())
                    .await()

                uiState = uiState.copy(
                    nombre = nombreLimpio,
                    correo = perfilActualizado.correo,
                    correoVerificado = usuario.isEmailVerified,
                    fechaRegistro = perfilActualizado.fechaRegistro,
                    rol = perfilActualizado.rol,
                    guardandoDatos = false,
                    error = "",
                    mensaje = "Datos actualizados correctamente"
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    guardandoDatos = false,
                    error = "No se pudo actualizar la cuenta"
                )
            }
        }
    }

    fun subirFotoPerfil(uri: Uri) {
        val usuario = firebaseAuth.currentUser

        if (usuario == null) {
            uiState = uiState.copy(error = "No hay una sesión activa")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                subiendoFoto = true,
                error = "",
                mensaje = ""
            )

            try {
                val fotoRef = storage
                    .reference
                    .child("usuarios/${usuario.uid}/perfil/foto_perfil.jpg")

                fotoRef.putFile(uri).await()

                val fotoUrl = fotoRef.downloadUrl.await().toString()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(uiState.nombre.trim())
                    .setPhotoUri(Uri.parse(fotoUrl))
                    .build()

                usuario.updateProfile(profileUpdates).await()

                firestore
                    .collection("usuarios")
                    .document(usuario.uid)
                    .set(mapOf("fotoPerfil" to fotoUrl), SetOptions.merge())
                    .await()

                uiState = uiState.copy(
                    fotoPerfil = fotoUrl,
                    subiendoFoto = false,
                    mensaje = "Foto de perfil actualizada correctamente",
                    error = ""
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    subiendoFoto = false,
                    error = "No se pudo subir la foto de perfil"
                )
            }
        }
    }

    fun abrirDialogCorreo() {
        uiState = uiState.copy(
            mostrarDialogCorreo = true,
            nuevoCorreo = uiState.correo,
            passwordActual = "",
            error = "",
            mensaje = ""
        )
    }

    fun cerrarDialogCorreo() {
        uiState = uiState.copy(
            mostrarDialogCorreo = false,
            nuevoCorreo = "",
            passwordActual = "",
            actualizandoCorreo = false,
            error = ""
        )
    }

    fun abrirDialogPassword() {
        uiState = uiState.copy(
            mostrarDialogPassword = true,
            passwordActual = "",
            nuevoPassword = "",
            confirmarNuevoPassword = "",
            error = "",
            mensaje = ""
        )
    }

    fun cerrarDialogPassword() {
        uiState = uiState.copy(
            mostrarDialogPassword = false,
            passwordActual = "",
            nuevoPassword = "",
            confirmarNuevoPassword = "",
            actualizandoPassword = false,
            error = ""
        )
    }

    fun abrirDialogEliminarCuenta() {
        uiState = uiState.copy(
            mostrarDialogEliminarCuenta = true,
            passwordActual = "",
            error = "",
            mensaje = ""
        )
    }

    fun cerrarDialogEliminarCuenta() {
        uiState = uiState.copy(
            mostrarDialogEliminarCuenta = false,
            passwordActual = "",
            eliminandoCuenta = false,
            error = ""
        )
    }

    fun onPasswordActualChange(value: String) {
        uiState = uiState.copy(
            passwordActual = value,
            error = "",
            mensaje = ""
        )
    }

    fun onNuevoCorreoChange(value: String) {
        uiState = uiState.copy(
            nuevoCorreo = value,
            error = "",
            mensaje = ""
        )
    }

    fun onNuevoPasswordChange(value: String) {
        uiState = uiState.copy(
            nuevoPassword = value,
            error = "",
            mensaje = ""
        )
    }

    fun onConfirmarNuevoPasswordChange(value: String) {
        uiState = uiState.copy(
            confirmarNuevoPassword = value,
            error = "",
            mensaje = ""
        )
    }

    private suspend fun reautenticarUsuario(password: String) {
        val usuario = firebaseAuth.currentUser
            ?: throw IllegalStateException("No hay una sesión activa")

        val correoActual = usuario.email
            ?: throw IllegalStateException("No hay correo asociado a esta cuenta")

        val credential = EmailAuthProvider.getCredential(
            correoActual,
            password
        )

        usuario.reauthenticate(credential).await()
    }

    fun actualizarCorreo() {
        val usuario = firebaseAuth.currentUser

        if (usuario == null) {
            uiState = uiState.copy(error = "No hay una sesión activa")
            return
        }

        val nuevoCorreoLimpio = uiState.nuevoCorreo.trim()
        val password = uiState.passwordActual

        if (nuevoCorreoLimpio.isBlank()) {
            uiState = uiState.copy(error = "Ingresa el nuevo correo")
            return
        }

        if (password.isBlank()) {
            uiState = uiState.copy(error = "Ingresa tu contraseña actual")
            return
        }

        if (nuevoCorreoLimpio.equals(uiState.correo.trim(), ignoreCase = true)) {
            uiState = uiState.copy(error = "Ese ya es tu correo actual")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                actualizandoCorreo = true,
                error = "",
                mensaje = ""
            )

            try {
                reautenticarUsuario(password)
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    actualizandoCorreo = false,
                    error = "La contraseña actual es incorrecta"
                )
                return@launch
            }

            try {
                usuario.verifyBeforeUpdateEmail(nuevoCorreoLimpio).await()

                uiState = uiState.copy(
                    nuevoCorreo = "",
                    passwordActual = "",
                    mostrarDialogCorreo = false,
                    actualizandoCorreo = false,
                    mensaje = "Te enviamos un correo de verificación para cambiar tu email",
                    error = ""
                )
            } catch (exception: Exception) {
                val mensajeError = when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        "Este correo ya está registrado en otra cuenta"
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        "El nuevo correo no tiene un formato válido"
                    }

                    is FirebaseAuthRecentLoginRequiredException -> {
                        "Por seguridad, vuelve a iniciar sesión e intenta nuevamente"
                    }

                    else -> {
                        exception.localizedMessage ?: "No se pudo enviar la verificación al nuevo correo"
                    }
                }

                uiState = uiState.copy(
                    actualizandoCorreo = false,
                    error = mensajeError
                )
            }
        }
    }

    fun actualizarPassword() {
        val usuario = firebaseAuth.currentUser

        if (usuario == null) {
            uiState = uiState.copy(error = "No hay una sesión activa")
            return
        }

        val passwordActual = uiState.passwordActual
        val nuevoPassword = uiState.nuevoPassword
        val confirmarNuevoPassword = uiState.confirmarNuevoPassword

        if (passwordActual.isBlank()) {
            uiState = uiState.copy(error = "Ingresa tu contraseña actual")
            return
        }

        if (nuevoPassword.length < 6) {
            uiState = uiState.copy(error = "La nueva contraseña debe tener al menos 6 caracteres")
            return
        }

        if (nuevoPassword != confirmarNuevoPassword) {
            uiState = uiState.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                actualizandoPassword = true,
                error = "",
                mensaje = ""
            )

            try {
                reautenticarUsuario(passwordActual)

                usuario.updatePassword(nuevoPassword).await()

                uiState = uiState.copy(
                    passwordActual = "",
                    nuevoPassword = "",
                    confirmarNuevoPassword = "",
                    mostrarDialogPassword = false,
                    actualizandoPassword = false,
                    mensaje = "Contraseña actualizada correctamente",
                    error = ""
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    actualizandoPassword = false,
                    error = "No se pudo actualizar la contraseña. Verifica tu contraseña actual"
                )
            }
        }
    }

    fun eliminarCuenta() {
        val usuario = firebaseAuth.currentUser

        if (usuario == null) {
            uiState = uiState.copy(error = "No hay una sesión activa")
            return
        }

        val password = uiState.passwordActual

        if (password.isBlank()) {
            uiState = uiState.copy(error = "Ingresa tu contraseña actual")
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                eliminandoCuenta = true,
                error = "",
                mensaje = ""
            )

            try {
                reautenticarUsuario(password)
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    eliminandoCuenta = false,
                    error = "La contraseña actual es incorrecta"
                )
                return@launch
            }

            try {
                firestore
                    .collection("usuarios")
                    .document(usuario.uid)
                    .delete()
                    .await()

                storage
                    .reference
                    .child("usuarios/${usuario.uid}/perfil/foto_perfil.jpg")
                    .delete()
                    .await()
            } catch (exception: Exception) {
                // Los datos secundarios no deben bloquear la eliminación de Auth.
            }

            try {
                usuario.delete().await()

                uiState = uiState.copy(
                    mostrarDialogEliminarCuenta = false,
                    eliminandoCuenta = false,
                    cuentaEliminada = true,
                    error = "",
                    mensaje = "Cuenta eliminada correctamente"
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    eliminandoCuenta = false,
                    error = "No se pudo eliminar la cuenta"
                )
            }
        }
    }

    fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}
