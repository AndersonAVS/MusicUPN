package com.example.appmusicupn.ui.cuenta

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmusicupn.viewmodel.CuentaViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
fun PantallaConfiguracionCuenta(
    navController: NavController,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val uiState=cuentaViewModel.uiState
    val usuario = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Configuración de la cuenta",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = cuentaViewModel::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.LightGray,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Nombre: ${uiState.nombre.ifBlank { "Sin Nombre" }}",
            color = Color.White
        )

        Text(
            text = "Correo: ${uiState.correo.ifBlank { "Sin correo" }}",
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "ID: ${uiState.uid.ifBlank { "Sin sesión" }}",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )

        Button(
            onClick = cuentaViewModel::guardarCambios,
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.guardando){
                CircularProgressIndicator()
            }else{
                Text("Guardar Cambios")
            }
        }

        if (uiState.error.isNotEmpty()){
            Spacer(modifier = Modifier.height(8.dp))
            Text(uiState.error, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.mensaje.isNotEmpty()){
            Spacer(modifier = Modifier.height(8.dp))
            Text(uiState.mensaje, color = Color(0xFF81C784))
        }

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

