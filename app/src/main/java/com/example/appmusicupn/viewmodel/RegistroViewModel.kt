package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appmusicupn.data.repository.AuthRepository
import com.example.appmusicupn.data.repository.RepositoryProvider
import com.example.appmusicupn.data.repository.RepositoryResult

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val mensajeError: String = "",
    val registroExitoso: Boolean = false
)

class RegistroViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {
    var uiState by mutableStateOf(RegistroUiState())
        private set

    fun onNombreChange(value: String) {
        uiState = uiState.copy(nombre = value, mensajeError = "")
    }

    fun onCorreoChange(value: String) {
        uiState = uiState.copy(correo = value, mensajeError = "")
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, mensajeError = "")
    }

    fun onConfirmarPasswordChange(value: String) {
        uiState = uiState.copy(confirmarPassword = value, mensajeError = "")
    }

    fun registrar() {
        val error = validarCampos()
        if (error != null) {
            uiState = uiState.copy(mensajeError = error)
            return
        }

        viewModelScope.launch {
            when (
                val result = authRepository.registrar(
                    nombre = uiState.nombre,
                    correo = uiState.correo,
                    password = uiState.password
                )
            ) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(mensajeError = "", registroExitoso = true)
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(mensajeError = result.message)
                }
            }
        }
    }
    private fun validarCampos(): String? = when {
        uiState.nombre.isBlank() -> "Ingrese su nombre completo"
        uiState.correo.isBlank() -> "Ingrese su correo electrónico"
        uiState.password.isBlank() -> "Ingrese una contraseña"
        uiState.confirmarPassword.isBlank() -> "Confirme su contraseña"
        uiState.password != uiState.confirmarPassword -> "Las contraseñas no coinciden"
        else -> null
    }
}
