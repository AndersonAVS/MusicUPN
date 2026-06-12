package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.repository.MusicRepository
import com.example.appmusicupn.data.repository.RepositoryProvider
import com.example.appmusicupn.data.repository.RepositoryResult
import kotlinx.coroutines.launch

data class DetallePlaylistUiState(
    val playlist: Playlist? = null,
    val canciones: List<Cancion> = emptyList(),
    val busqueda: String = "",
    val ordenarAscendente: Boolean = true,
    val cargando: Boolean = false,
    val error: String = "",
    val mensaje: String = ""
)

class DetallePlaylistViewModel(
    private val musicRepository: MusicRepository = RepositoryProvider.musicRepository
) : ViewModel() {

    var uiState by mutableStateOf(DetallePlaylistUiState())
        private set

    fun cargarPlaylist(
        playlist: Playlist
    ) {
        uiState = uiState.copy(
            playlist = playlist,
            error = "",
            mensaje = ""
        )

        cargarCanciones(playlist.id)
    }

    private fun cargarCanciones(
        playlistId: String
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true)

            when (val result = musicRepository.obtenerCancionesDePlaylist(playlistId)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        canciones = result.data,
                        cargando = false,
                        error = ""
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(
                        cargando = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun onBusquedaChange(value: String) {
        uiState = uiState.copy(
            busqueda = value,
            error = "",
            mensaje = ""
        )
    }

    fun alternarOrden() {
        uiState = uiState.copy(
            ordenarAscendente = !uiState.ordenarAscendente
        )
    }

    fun eliminarCancion(cancionId: String) {
        val playlistActual = uiState.playlist ?: return

        viewModelScope.launch {
            when (
                val result = musicRepository.eliminarCancionDePlaylist(
                    playlistId = playlistActual.id,
                    cancionId = cancionId
                )
            ) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        canciones = uiState.canciones.filter { cancion ->
                            cancion.id != cancionId
                        },
                        mensaje = "Canción quitada de la playlist",
                        error = ""
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(
                        error = result.message,
                        mensaje = ""
                    )
                }
            }
        }
    }
}
