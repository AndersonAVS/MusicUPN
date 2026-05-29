package com.example.appmusicupn.ui.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmusicupn.components.BibliotecaItem
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.ChipBiblioteca
import com.example.appmusicupn.components.MiniPlayer
import com.example.appmusicupn.viewmodel.BibliotecaViewModel
import com.example.appmusicupn.viewmodel.HomeViewModel

@Composable
fun PantallaBiblioteca(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    bibliotecaViewModel: BibliotecaViewModel = viewModel()
) {
    val bibliotecaState = bibliotecaViewModel.uiState
    var mostrarMenuCuenta by remember { mutableStateOf(false) }
    var mostrarDialogCrearPlaylist by remember { mutableStateOf(false) }

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
                            navController.navigate("configuracion_cuenta")
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
                text = "Tu biblioteca",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            ChipBiblioteca("Playlists")
            ChipBiblioteca("Podcasts")
            ChipBiblioteca("Álbumes")
            ChipBiblioteca("Artistas")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Recientes",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (bibliotecaState.playlists.isEmpty()) {
            Text(
                text = "Aún no tienes playlists creadas",
                color = Color.Gray
            )
        } else {
            bibliotecaState.playlists.forEach { playlist ->
                BibliotecaItem(
                    titulo = playlist.nombre,
                    descripcion = playlist.descripcion,
                    icono = "♫",
                    color = Color(0xFF374151),
                    onClick = {
                        bibliotecaViewModel.seleccionarPlaylistParaEditar(playlist)
                    }
                )
            }
        }

        if (bibliotecaState.error.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bibliotecaState.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (bibliotecaState.mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bibliotecaState.mensaje,
                color = Color(0xFF81C784)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        MiniPlayer()

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(
            navController = navController,
            pantallaActual = "biblioteca",
            onCrearPlaylist = {
                mostrarDialogCrearPlaylist = true
            }
        )
    }

    if (mostrarDialogCrearPlaylist) {
        AlertDialog(
            onDismissRequest = { mostrarDialogCrearPlaylist = false },
            title = { Text("Crear playlist") },
            text = {
                OutlinedTextField(
                    value = bibliotecaState.nombreNuevaPlaylist,
                    onValueChange = bibliotecaViewModel::onNombreNuevaPlaylistChange,
                    label = { Text("Nombre de playlist") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bibliotecaViewModel.crearPlaylist()
                        mostrarDialogCrearPlaylist = false
                    }
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogCrearPlaylist = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (bibliotecaState.playlistEditando != null) {
        AlertDialog(
            onDismissRequest = {
                bibliotecaViewModel.cancelarEdicionPlaylist()
            },
            title = { Text("Editar playlist") },
            text = {
                Column {
                    OutlinedTextField(
                        value = bibliotecaState.nombreEditando,
                        onValueChange = bibliotecaViewModel::onNombreEditandoChange,
                        label = { Text("Nombre") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bibliotecaState.descripcionEditando,
                        onValueChange = bibliotecaViewModel::onDescripcionEditandoChange,
                        label = { Text("Descripción") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bibliotecaViewModel.guardarEdicionPlaylist()
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            bibliotecaViewModel.solicitarEliminarPlaylist()
                        }
                    ) {
                        Text(
                            text = "Eliminar",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    TextButton(
                        onClick = {
                            bibliotecaViewModel.cancelarEdicionPlaylist()
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

    if (bibliotecaState.playlistPendienteEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                bibliotecaViewModel.cancelarEliminarPlaylist()
            },
            title = { Text("Eliminar playlist") },
            text = {
                Text(
                    text = "¿Seguro que quieres eliminar \"${bibliotecaState.playlistPendienteEliminar.nombre}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        bibliotecaViewModel.confirmarEliminarPlaylist()
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        bibliotecaViewModel.cancelarEliminarPlaylist()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
