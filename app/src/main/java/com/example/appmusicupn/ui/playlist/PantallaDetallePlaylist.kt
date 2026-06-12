package com.example.appmusicupn.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmusicupn.components.MiniPlayer
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.viewmodel.DetallePlaylistViewModel
import com.example.appmusicupn.viewmodel.PlaybackViewModel
import androidx.compose.material3.TextButton

@Composable
fun PantallaDetallePlaylist(
    navController: NavController,
    playbackViewModel: PlaybackViewModel,
    playlist: Playlist,
    detallePlaylistViewModel: DetallePlaylistViewModel = viewModel()
) {
    val detalleState = detallePlaylistViewModel.uiState
    val playbackState = playbackViewModel.uiState

    LaunchedEffect(playlist.id) {
        detallePlaylistViewModel.cargarPlaylist(playlist)
    }

    val cancionesFiltradas = detalleState.canciones
        .filter { cancion ->
            cancion.titulo.contains(detalleState.busqueda, ignoreCase = true) ||
                cancion.artista.contains(detalleState.busqueda, ignoreCase = true) ||
                cancion.album.contains(detalleState.busqueda, ignoreCase = true)
        }
        .let { lista ->
            if (detalleState.ordenarAscendente) {
                lista.sortedBy { cancion -> cancion.titulo.lowercase() }
            } else {
                lista.sortedByDescending { cancion -> cancion.titulo.lowercase() }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF374151),
                            Color(0xFF121212),
                            Color(0xFF121212)
                        )
                    )
                )
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .background(
                                Color.White.copy(alpha = 0.16f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = detalleState.busqueda,
                            onValueChange = detallePlaylistViewModel::onBusquedaChange,
                            placeholder = {
                                Text("Buscar en playlist")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .height(52.dp)
                            .background(
                                Color.White.copy(alpha = 0.16f),
                                RoundedCornerShape(6.dp)
                            )
                            .clickable {
                                detallePlaylistViewModel.alternarOrden()
                            }
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ordenar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(96.dp))

                Text(
                    text = playlist.nombre,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = playlist.descripcion.ifBlank { "Playlist creada por ti" },
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${cancionesFiltradas.size} canciones",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF374151), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♫",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Aleatorio",
                        tint = Color(0xFF1DB954),
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    IconButton(
                        onClick = {
                            val primeraCancion = cancionesFiltradas.firstOrNull()

                            if (primeraCancion != null) {
                                playbackViewModel.reproducirCancion(primeraCancion)
                            }
                        },
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF1DB954), RoundedCornerShape(50))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reproducir",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                if (detalleState.error.isNotEmpty()) {
                    Text(
                        text = detalleState.error,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (detalleState.mensaje.isNotEmpty()) {
                    Text(
                        text = detalleState.mensaje,
                        color = Color(0xFF81C784)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (cancionesFiltradas.isEmpty()) {
                item {
                    Text(
                        text = "Esta playlist aún no tiene canciones",
                        color = Color.LightGray
                    )
                }
            } else {
                items(cancionesFiltradas) { cancion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                playbackViewModel.reproducirCancion(cancion)
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(Color(0xFF2A2A2A), RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("♪", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = cancion.titulo,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )

                            Text(
                                text = cancion.artista,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }

                        TextButton(
                            onClick = {
                                detallePlaylistViewModel.eliminarCancion(cancion.id)
                            }
                        ) {
                            Text(
                                text = "Quitar",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }

        MiniPlayer(
            cancionActual = playbackState.cancionActual,
            reproduciendo = playbackState.reproduciendo,
            onPlayPauseClick = playbackViewModel::alternarPlayPause,
            onStopClick = playbackViewModel::detenerReproduccion
        )
    }
}
