package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.appmusicupn.data.model.UsuarioPerfil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

data class CuentaUiState(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val fotoPerfil: String = "",
    val fechaRegistro: Long = 0L,
    val rol: String = "usuario",
    val error: String = "",
    val mensaje: String = "",
    val guardandoDatos: Boolean = false,
    val subiendoFoto: Boolean = false
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
                val snapshot = firestore
                    .collection("usuarios")
                    .document(usuario.uid)
                    .get()
                    .await()

                val perfil = snapshot.toObject(UsuarioPerfil::class.java)

                uiState = uiState.copy(
                    uid = usuario.uid,
                    nombre = perfil?.nombre ?: usuario.displayName.orEmpty(),
                    correo = perfil?.correo ?: usuario.email.orEmpty(),
                    fotoPerfil = perfil?.fotoPerfil ?: usuario.photoUrl?.toString().orEmpty(),
                    fechaRegistro = perfil?.fechaRegistro ?: 0L,
                    rol = perfil?.rol ?: "usuario"
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    uid = usuario.uid,
                    nombre = usuario.displayName.orEmpty(),
                    correo = usuario.email.orEmpty(),
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

                val profileUpdates = profileBuilder.build()

                usuario.updateProfile(profileUpdates).await()

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
                    fechaRegistro = perfilActualizado.fechaRegistro,
                    rol = perfilActualizado.rol,
                    guardandoDatos  = false,
                    error = "",
                    mensaje = "Datos actualizados correctamente"
                )

            } catch (exception: Exception) {
                uiState = uiState.copy(
                    guardandoDatos  = false,
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
                subiendoFoto  = true,
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
                    .update("fotoPerfil", fotoUrl)
                    .await()

                uiState = uiState.copy(
                    fotoPerfil = fotoUrl,
                    subiendoFoto  = false,
                    mensaje = "Foto de perfil actualizada correctamente",
                    error = ""
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    subiendoFoto  = false,
                    error = "No se pudo subir la foto de perfil"
                )
            }
        }
    }
}
