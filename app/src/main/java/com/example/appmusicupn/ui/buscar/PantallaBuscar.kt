package com.example.appmusicupn.ui.buscar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.CategoriaCard
import com.example.appmusicupn.components.MiniPlayer
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmusicupn.viewmodel.HomeViewModel

@Composable
fun PantallaBuscar(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {

    var mostrarMenuCuenta by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    text = "A",
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color(0xFFB39DDB), RoundedCornerShape(50))
                        .clickable { mostrarMenuCuenta = true }
                        .padding(14.dp)
                )

                DropdownMenu(
                    expanded = mostrarMenuCuenta,
                    onDismissRequest = { mostrarMenuCuenta = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Configuración de la cuenta") },
                        onClick = {
                            mostrarMenuCuenta = false
                            // Luego navegaremos a una pantalla de configuración
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            mostrarMenuCuenta = false
                            homeViewModel.cerrarSesion()

                            navController.navigate("inicio") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Buscar",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "¿Qué quieres escuchar?",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Descubre algo nuevo para ti",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            CategoriaCard("#reggaeton", Color(0xFF6D1B1B))
            CategoriaCard("#urbano latino", Color(0xFF1E3A8A))
            CategoriaCard("#sunrise", Color(0xFFB45309))
        }

        Spacer(modifier = Modifier.weight(1f))

        MiniPlayer()

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(navController, "buscar")
    }
}
