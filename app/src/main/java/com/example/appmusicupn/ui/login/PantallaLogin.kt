package com.example.appmusicupn.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmusicupn.data.model.UserRole
import com.example.appmusicupn.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val uiState = loginViewModel.uiState

    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            val destino = when (uiState.rol) {
                UserRole.ADMIN -> "home"
                UserRole.USER -> "home"
                null -> "home"
            }

            navController.navigate(destino) {
                popUpTo("inicio") {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.correo,
            onValueChange = loginViewModel::onCorreoChange,
            label = { Text("Usuario o correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = loginViewModel::onPasswordChange,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = loginViewModel::login,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        if (uiState.error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(uiState.error, color = MaterialTheme.colorScheme.error)
        }
    }
}
