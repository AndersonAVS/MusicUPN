package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.repository.MusicRepository
import com.example.appmusicupn.data.repository.RepositoryProvider
import com.example.appmusicupn.data.repository.RepositoryResult
import kotlinx.coroutines.launch
import com.example.appmusicupn.data.model.Favorito

data class BibliotecaUiState(
    val playlists: List<Playlist> = emptyList(),
    val favoritos: List<Favorito> = emptyList(),
    val nombreNuevaPlaylist: String = "",
    val error: String = "",
    val mensaje: String = "",
    val cargando: Boolean = false,
    val playlistEditando: Playlist? = null,
    val nombreEditando: String = "",
    val descripcionEditando: String = "",
    val playlistPendienteEliminar: Playlist? = null,
    val playlistOpciones: Playlist? = null,
)

class BibliotecaViewModel(
    private val musicRepository: MusicRepository = RepositoryProvider.musicRepository
) : ViewModel() {

    var uiState by mutableStateOf(BibliotecaUiState())
        private set

    init {
        cargarBiblioteca()
    }


    fun onNombreNuevaPlaylistChange(value: String) {
        uiState = uiState.copy(
            nombreNuevaPlaylist = value,
            error = "",
            mensaje = ""
        )
    }

    fun cargarPlaylists() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true)

            when (val result = musicRepository.obtenerPlaylistsUsuario()) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        playlists = result.data,
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

    fun crearPlaylist() {
        val nombre = uiState.nombreNuevaPlaylist.trim()

        if (nombre.isBlank()) {
            uiState = uiState.copy(error = "Ingresa un nombre para la playlist")
            return
        }

        viewModelScope.launch {
            when (val result = musicRepository.crearPlaylist(nombre)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        playlists = uiState.playlists + result.data,
                        nombreNuevaPlaylist = "",
                        mensaje = "Playlist creada correctamente",
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
    fun abrirOpcionesPlaylist(playlist: Playlist) {
        uiState = uiState.copy(
            playlistOpciones = playlist,
            error = "",
            mensaje = ""
        )
    }
    fun cerrarOpcionesPlaylist() {
        uiState = uiState.copy(
            playlistOpciones = null
        )
    }

    fun seleccionarPlaylistParaEditar(playlist: Playlist) {
        uiState = uiState.copy(
            playlistEditando = playlist,
            playlistOpciones = null,
            nombreEditando = playlist.nombre,
            descripcionEditando = playlist.descripcion,
            error = "",
            mensaje = ""
        )
    }

    fun cancelarEdicionPlaylist() {
        uiState = uiState.copy(
            playlistEditando = null,
            nombreEditando = "",
            descripcionEditando = "",
            error = "",
            mensaje = ""
        )
    }


    fun onNombreEditandoChange(value: String) {
        uiState = uiState.copy(
            nombreEditando = value,
            error = "",
            mensaje = ""
        )
    }

    fun onDescripcionEditandoChange(value: String) {
        uiState = uiState.copy(
            descripcionEditando = value,
            error = "",
            mensaje = ""
        )
    }

    fun guardarEdicionPlaylist() {
        val playlistActual = uiState.playlistEditando ?: return
        val nombre = uiState.nombreEditando.trim()
        val descripcion = uiState.descripcionEditando.trim()

        if (nombre.isBlank()) {
            uiState = uiState.copy(error = "El nombre no puede estar vacío")
            return
        }

        val playlistActualizada = playlistActual.copy(
            nombre = nombre,
            descripcion = descripcion.ifBlank { "Playlist creada por ti" }
        )

        viewModelScope.launch {
            when (val result = musicRepository.actualizarPlaylist(playlistActualizada)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        playlists = uiState.playlists.map { playlist ->
                            if (playlist.id == result.data.id) result.data else playlist
                        },
                        playlistEditando = null,
                        nombreEditando = "",
                        descripcionEditando = "",
                        error = "",
                        mensaje = "Playlist actualizada correctamente"
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

    fun solicitarEliminarPlaylist() {
        val playlist = uiState.playlistEditando
            ?: uiState.playlistOpciones
            ?: return

        uiState = uiState.copy(
            playlistPendienteEliminar = playlist,
            playlistOpciones = null
        )
    }
    fun cancelarEliminarPlaylist() {
        uiState = uiState.copy(
            playlistPendienteEliminar = null
        )
    }
    fun confirmarEliminarPlaylist() {
        val playlist = uiState.playlistPendienteEliminar ?: return

        viewModelScope.launch {
            when (val result = musicRepository.eliminarPlaylist(playlist.id)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        playlists = uiState.playlists.filter { it.id != playlist.id },
                        playlistEditando = null,
                        playlistPendienteEliminar = null,
                        nombreEditando = "",
                        descripcionEditando = "",
                        error = "",
                        mensaje = "Playlist eliminada correctamente"
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(
                        playlistPendienteEliminar = null,
                        error = result.message,
                        mensaje = ""
                    )
                }
            }
        }
    }

    fun cargarBiblioteca() {
        cargarPlaylists()
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            uiState = uiState.copy(cargando = true)

            when (val result = musicRepository.obtenerFavoritosUsuario()) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        favoritos = result.data,
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
}

