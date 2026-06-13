package com.example.appmusicupn.ui.cuenta

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmusicupn.viewmodel.CuentaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaConfiguracionCuenta(
    navController: NavController,
    cuentaViewModel: CuentaViewModel = viewModel()
) {
    val uiState = cuentaViewModel.uiState
    val fechaRegistroTexto = formatearFechaRegistro(uiState.fechaRegistro)

    LaunchedEffect(uiState.cuentaEliminada) {
        if (uiState.cuentaEliminada) {
            navController.navigate("inicio") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    }

    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                cuentaViewModel.subirFotoPerfil(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color(0xFF1F1F1F), RoundedCornerShape(48.dp))
                        .clip(RoundedCornerShape(48.dp))
                        .clickable {
                            selectorFoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.fotoPerfil.isBlank()) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Seleccionar foto de perfil",
                            tint = Color.White,
                            modifier = Modifier.size(72.dp)
                        )
                    } else {
                        AsyncImage(
                            model = uiState.fotoPerfil,
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(96.dp)
                        )
                    }
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

                    Text(
                        text = "Toca la imagen para cambiar tu foto",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.subiendoFoto) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Subiendo foto de perfil...",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (uiState.mensaje.isNotEmpty()) {
                Text(
                    text = uiState.mensaje,
                    color = Color(0xFF81C784),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (uiState.error.isNotEmpty() &&
                !uiState.mostrarDialogCorreo &&
                !uiState.mostrarDialogPassword &&
                !uiState.mostrarDialogEliminarCuenta
            ) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        RecuadroCuenta(
            titulo = "Nombre de usuario",
            valor = uiState.nombre.ifBlank { uiState.uid },
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecuadroCuenta(
            titulo = "Email",
            valor = uiState.correo.ifBlank { "Sin correo" },
            detalle = if (uiState.correoVerificado) {
                "Correo verificado"
            } else {
                "Correo no verificado"
            },
            onClick = cuentaViewModel::abrirDialogCorreo
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecuadroCuenta(
            titulo = "Contraseña",
            valor = "********",
            detalle = "Toca para cambiar tu contraseña",
            onClick = cuentaViewModel::abrirDialogPassword
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecuadroCuenta(
            titulo = "Fecha de registro",
            valor = fechaRegistroTexto,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(36.dp))

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
            colors = campoTextoCuentaColors()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = cuentaViewModel::guardarCambios,
            enabled = !uiState.guardandoDatos,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.guardandoDatos) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Guardar Cambios")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                cuentaViewModel.cerrarSesion()

                navController.navigate("inicio") {
                    popUpTo("home") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = cuentaViewModel::abrirDialogEliminarCuenta,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar cuenta")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }

    if (uiState.mostrarDialogCorreo) {
        DialogCambiarCorreo(
            passwordActual = uiState.passwordActual,
            nuevoCorreo = uiState.nuevoCorreo,
            error = uiState.error,
            actualizando = uiState.actualizandoCorreo,
            onPasswordChange = cuentaViewModel::onPasswordActualChange,
            onCorreoChange = cuentaViewModel::onNuevoCorreoChange,
            onActualizar = cuentaViewModel::actualizarCorreo,
            onCancelar = cuentaViewModel::cerrarDialogCorreo
        )
    }

    if (uiState.mostrarDialogPassword) {
        DialogCambiarPassword(
            passwordActual = uiState.passwordActual,
            nuevoPassword = uiState.nuevoPassword,
            confirmarNuevoPassword = uiState.confirmarNuevoPassword,
            error = uiState.error,
            actualizando = uiState.actualizandoPassword,
            onPasswordActualChange = cuentaViewModel::onPasswordActualChange,
            onNuevoPasswordChange = cuentaViewModel::onNuevoPasswordChange,
            onConfirmarPasswordChange = cuentaViewModel::onConfirmarNuevoPasswordChange,
            onActualizar = cuentaViewModel::actualizarPassword,
            onCancelar = cuentaViewModel::cerrarDialogPassword
        )
    }

    if (uiState.mostrarDialogEliminarCuenta) {
        DialogEliminarCuenta(
            passwordActual = uiState.passwordActual,
            error = uiState.error,
            eliminando = uiState.eliminandoCuenta,
            onPasswordChange = cuentaViewModel::onPasswordActualChange,
            onEliminar = cuentaViewModel::eliminarCuenta,
            onCancelar = cuentaViewModel::cerrarDialogEliminarCuenta
        )
    }
}

@Composable
private fun RecuadroCuenta(
    titulo: String,
    valor: String,
    detalle: String = "",
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F1F1F), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = titulo,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = valor,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        if (detalle.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = detalle,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun DialogCambiarCorreo(
    passwordActual: String,
    nuevoCorreo: String,
    error: String,
    actualizando: Boolean,
    onPasswordChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onActualizar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Cambiar correo") },
        text = {
            Column {
                Text("¿Deseas cambiar tu correo? Para continuar, ingresa tu contraseña actual.")

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordActual,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña actual") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nuevoCorreo,
                    onValueChange = onCorreoChange,
                    label = { Text("Nuevo correo") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                MensajeErrorDialog(error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onActualizar,
                enabled = !actualizando
            ) {
                if (actualizando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Actualizar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DialogCambiarPassword(
    passwordActual: String,
    nuevoPassword: String,
    confirmarNuevoPassword: String,
    error: String,
    actualizando: Boolean,
    onPasswordActualChange: (String) -> Unit,
    onNuevoPasswordChange: (String) -> Unit,
    onConfirmarPasswordChange: (String) -> Unit,
    onActualizar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Cambiar contraseña") },
        text = {
            Column {
                Text("Para cambiar tu contraseña, confirma primero tu contraseña actual.")

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordActual,
                    onValueChange = onPasswordActualChange,
                    label = { Text("Contraseña actual") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nuevoPassword,
                    onValueChange = onNuevoPasswordChange,
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmarNuevoPassword,
                    onValueChange = onConfirmarPasswordChange,
                    label = { Text("Confirmar nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                MensajeErrorDialog(error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onActualizar,
                enabled = !actualizando
            ) {
                if (actualizando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Actualizar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DialogEliminarCuenta(
    passwordActual: String,
    error: String,
    eliminando: Boolean,
    onPasswordChange: (String) -> Unit,
    onEliminar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Eliminar cuenta") },
        text = {
            Column {
                Text("Esta acción eliminará tu cuenta de forma permanente. Ingresa tu contraseña para confirmar.")

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordActual,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña actual") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                MensajeErrorDialog(error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onEliminar,
                enabled = !eliminando
            ) {
                if (eliminando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Eliminar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun MensajeErrorDialog(error: String) {
    if (error.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun campoTextoCuentaColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.LightGray,
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.Gray,
    cursorColor = Color.White
)

private fun formatearFechaRegistro(fechaRegistro: Long): String {
    if (fechaRegistro == 0L) {
        return "Sin fecha registrada"
    }

    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formato.format(Date(fechaRegistro))
}
