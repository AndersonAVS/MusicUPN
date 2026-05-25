package com.example.appmusicupn.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun BottomMenu( navController: NavController,
                pantallaActual: String,
                onCrearPlaylist: () -> Unit = {})
{
    var mostrarCrear by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MenuItem(Icons.Default.Home, "Inicio", pantallaActual == "home") {
            navController.navigate("home")
        }

        MenuItem(Icons.Default.Search, "Buscar", pantallaActual == "buscar") {
            navController.navigate("buscar")
        }

        MenuItem(Icons.Default.LibraryMusic, "Tu biblioteca", pantallaActual == "biblioteca") {
            navController.navigate("biblioteca")
        }

        MenuItem(Icons.Default.Add, "Crear", false) {
            mostrarCrear = true
        }
    }

    CrearBottomSheet(
        mostrar = mostrarCrear,
        onCerrar = { mostrarCrear = false },
        onCrearPlaylist = {
            mostrarCrear = false
            onCrearPlaylist()
        }
    )
}

@Composable
fun MenuItem(
    icon: ImageVector,
    texto: String,
    activo: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (activo) Color.White else Color.Gray
        )

        Text(
            texto,
            color = if (activo) Color.White else Color.Gray
        )
    }
}

