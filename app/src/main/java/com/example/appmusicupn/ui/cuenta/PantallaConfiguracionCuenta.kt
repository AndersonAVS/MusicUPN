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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

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



    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Cuenta",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Datos de la cuenta",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Nombre de usuario",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = uiState.nombre.ifBlank { uiState.uid },
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Email",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = uiState.correo.ifBlank { "Sin correo" },
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Rol",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = uiState.rol,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Tu plan",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFF1F1F1F), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = "Plan gratuito",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "MusicUPN Free",
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Editar perfil",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )


        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "ID: ${uiState.uid.ifBlank { "Sin sesión" }}",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
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

        Spacer(modifier = Modifier.height(24.dp))

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

