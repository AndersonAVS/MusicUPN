package com.example.appmusicupn.ui.buscar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.MiniPlayer
import com.example.appmusicupn.viewmodel.BuscarViewModel
import com.example.appmusicupn.viewmodel.HomeViewModel
import com.example.appmusicupn.viewmodel.PlaybackViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun PantallaBuscar(
    navController: NavController,
    playbackViewModel: PlaybackViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    buscarViewModel: BuscarViewModel = viewModel()
) {
    val buscarState = buscarViewModel.uiState
    val playbackState = playbackViewModel.uiState
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
                text = "Buscar",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = buscarState.query,
            onValueChange = buscarViewModel::onQueryChange,
            label = { Text("¿Qué quieres escuchar?") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = buscarViewModel::buscarCanciones,
            modifier = Modifier.fillMaxWidth(),
            enabled = !buscarState.cargando
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (buscarState.cargando) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (buscarState.error.isNotEmpty()) {
            Text(
                text = buscarState.error,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))
        }



        if (buscarState.mensaje.isNotEmpty()) {
            Text(
                text = buscarState.mensaje,
                color = Color(0xFF81C784)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (playbackState.error.isNotEmpty()) {
            Text(
                text = playbackState.error,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            buscarState.canciones.forEach { cancion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            playbackViewModel.reproducirCancion(cancion)
                        },
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = cancion.titulo,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = cancion.artista,
                            color = Color.Gray
                        )

                        if (cancion.album.isNotBlank()) {
                            Text(
                                text = cancion.album,
                                color = Color.DarkGray
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            buscarViewModel.abrirSelectorPlaylist(cancion)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar a playlist",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            buscarViewModel.agregarAFavoritos(cancion)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Agregar a favoritos",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        MiniPlayer(
            cancionActual = playbackState.cancionActual,
            reproduciendo = playbackState.reproduciendo,
            onPlayPauseClick = playbackViewModel::alternarPlayPause,
            onStopClick = playbackViewModel::detenerReproduccion,
            onNextClick = playbackViewModel::siguienteCancion,
            onPreviousClick = playbackViewModel::cancionAnterior
        )

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(navController, "buscar")
    }
    if (buscarState.mostrandoSelectorPlaylist) {
        AlertDialog(
            onDismissRequest = {
                buscarViewModel.cerrarSelectorPlaylist()
            },
            title = {
                Text("Agregar a playlist")
            },
            text = {
                Column {
                    if (buscarState.playlists.isEmpty()) {
                        Text("Aún no tienes playlists creadas")
                    } else {
                        buscarState.playlists.forEach { playlist ->
                            Text(
                                text = playlist.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        buscarViewModel.agregarCancionAPlaylist(playlist.id)
                                    }
                                    .padding(vertical = 12.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        buscarViewModel.cerrarSelectorPlaylist()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
