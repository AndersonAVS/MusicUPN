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

data class CuentaUiState(
    val nombre: String = "",
    val correo: String = "",
    val uid: String = "",
    val error: String = "",
    val mensaje: String = "",
    val guardando: Boolean = false
)

class CuentaViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    var uiState by mutableStateOf(CuentaUiState())
        private set

    init {
        cargarUsuario()
    }

    private fun cargarUsuario() {
        val usuario = firebaseAuth.currentUser

        uiState = uiState.copy(
            nombre = usuario?.displayName.orEmpty(),
            correo = usuario?.email.orEmpty(),
            uid = usuario?.uid.orEmpty()
        )
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
            uiState = uiState.copy(guardando = true, error = "", mensaje = "")

            try {
                val usuario = firebaseAuth.currentUser
                    ?: throw IllegalStateException("No hay una sesión activa")

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(nombreLimpio)
                    .build()

                usuario.updateProfile(profileUpdates).await()

                uiState = uiState.copy(
                    nombre = nombreLimpio,
                    guardando = false,
                    mensaje = "Datos actualizados correctamente"
                )
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    guardando = false,
                    error = "No se pudo actualizar la cuenta"
                )
            }
        }
    }
}
