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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appmusicupn.viewmodel.PlaybackViewModel
import com.example.appmusicupn.data.model.Cancion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBiblioteca(
    navController: NavController,
    playbackViewModel: PlaybackViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    bibliotecaViewModel: BibliotecaViewModel = viewModel(),


) {
    val bibliotecaState = bibliotecaViewModel.uiState
    var mostrarMenuCuenta by remember { mutableStateOf(false) }
    var mostrarDialogCrearPlaylist by remember { mutableStateOf(false) }
    val playbackState = playbackViewModel.uiState

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

        Text(
            text = "Canciones favoritas",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (bibliotecaState.favoritos.isEmpty()) {
            Text(
                text = "Aún no tienes canciones favoritas",
                color = Color.Gray
            )
        } else {

                BibliotecaItem(
                    titulo = "Favoritos",
                    descripcion = "Playlist • ${bibliotecaState.favoritos.size} canciones",
                    icono = "♥",
                    color = Color(0xFF5E6CE7),
                    onClick = {
                        navController.navigate("favoritos")
                    },
                    onMoreClick = {}
                )

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Playlists",
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
                    onClick = {navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("playlistSeleccionada", playlist)

                        navController.navigate("detalle_playlist")},
                    onMoreClick = {
                        bibliotecaViewModel.abrirOpcionesPlaylist(playlist)
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

        MiniPlayer(
            cancionActual = playbackState.cancionActual,
            reproduciendo = playbackState.reproduciendo,
            onPlayPauseClick = playbackViewModel::alternarPlayPause,
            onStopClick = playbackViewModel::detenerReproduccion,
            onNextClick = playbackViewModel::siguienteCancion,
            onPreviousClick = playbackViewModel::cancionAnterior
        )

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
    if (bibliotecaState.playlistOpciones != null) {
        ModalBottomSheet(
            onDismissRequest = {
                bibliotecaViewModel.cerrarOpcionesPlaylist()
            },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color(0xFF202020)
        ) {
            val playlist = bibliotecaState.playlistOpciones

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFF374151), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("♫", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = playlist.nombre,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Playlist creada por ti",
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OpcionPlaylistSheet(
                    icon = Icons.Default.Share,
                    texto = "Compartir",
                    onClick = {
                        bibliotecaViewModel.cerrarOpcionesPlaylist()
                    }
                )

                OpcionPlaylistSheet(
                    icon = Icons.Default.Edit,
                    texto = "Editar playlist",
                    onClick = {
                        bibliotecaViewModel.seleccionarPlaylistParaEditar(playlist)
                    }
                )

                OpcionPlaylistSheet(
                    icon = Icons.Default.Lock,
                    texto = "Hacer privada",
                    onClick = {
                        bibliotecaViewModel.cerrarOpcionesPlaylist()
                    }
                )

                OpcionPlaylistSheet(
                    icon = Icons.Default.PushPin,
                    texto = "Fijar playlist",
                    onClick = {
                        bibliotecaViewModel.cerrarOpcionesPlaylist()
                    }
                )

                OpcionPlaylistSheet(
                    icon = Icons.Default.Delete,
                    texto = "Eliminar playlist",
                    colorTexto = MaterialTheme.colorScheme.error,
                    onClick = {
                        bibliotecaViewModel.solicitarEliminarPlaylist()
                    }
                )
            }
        }
    }

}
@Composable
private fun OpcionPlaylistSheet(
    icon: ImageVector,
    texto: String,
    colorTexto: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = texto,
            color = colorTexto,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
