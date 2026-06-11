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

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val error: String = "",
    val loginExitoso: Boolean = false,
    val mensaje: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository = RepositoryProvider.authRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCorreoChange(value: String) {
        uiState = uiState.copy(correo = value, error = "", mensaje = "")
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, error = "", mensaje = "")
    }

    fun login() {
        val correo = uiState.correo.trim()
        val password = uiState.password

        if (correo.isBlank() || password.isBlank()) {
            uiState = uiState.copy(error = "Ingrese usuario y contraseña")
            return
        }

        viewModelScope.launch {
            when (val result = authRepository.login(correo, password)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        error = "",
                        loginExitoso = true
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(error = result.message)
                }
            }
        }
    }
    fun restablecerPassword() {
        val correo = uiState.correo.trim()

        if (correo.isBlank()) {
            uiState = uiState.copy(
                error = "Ingresa tu correo para restablecer tu contraseña",
                mensaje = ""
            )
            return
        }

        viewModelScope.launch {
            when (val result = authRepository.restablecerPassword(correo)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        error = "",
                        mensaje = "Te enviamos un correo para restablecer tu contraseña"
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(
                        error = result.message,
                        mensaje = ""
                    )
                }
            }
        }
    }

}
