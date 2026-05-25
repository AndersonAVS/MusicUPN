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

data class BibliotecaUiState(
    val playlists: List<Playlist> = emptyList(),
    val nombreNuevaPlaylist: String = "",
    val error: String = "",
    val mensaje: String = "",
    val cargando: Boolean = false
)

class BibliotecaViewModel(
    private val musicRepository: MusicRepository = RepositoryProvider.musicRepository
) : ViewModel() {

    var uiState by mutableStateOf(BibliotecaUiState())
        private set

    init {
        cargarPlaylists()
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
}

