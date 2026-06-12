package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.repository.MusicRepository
import com.example.appmusicupn.data.repository.RepositoryProvider
import com.example.appmusicupn.data.repository.RepositoryResult
import kotlinx.coroutines.launch

data class BuscarUiState(
    val query: String = "",
    val canciones: List<Cancion> = emptyList(),
    val cargando: Boolean = false,
    val mensaje: String = "",
    val error: String = ""
)

class BuscarViewModel(
    private val musicRepository: MusicRepository = RepositoryProvider.musicRepository
) : ViewModel() {

    var uiState by mutableStateOf(BuscarUiState())
        private set

    fun onQueryChange(value: String) {
        uiState = uiState.copy(
            query = value,
            error = "",
            mensaje = ""
        )
    }

    fun buscarCanciones() {
        val query = uiState.query.trim()

        if (query.isBlank()) {
            uiState = uiState.copy(
                error = "Ingresa una canción o artista para buscar",
                canciones = emptyList()
            )
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(
                cargando = true,
                error = ""
            )

            when (val result = musicRepository.buscarCancionesWeb(query)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        canciones = result.data,
                        cargando = false,
                        error = ""
                    )
                }

                is RepositoryResult.Error -> {
                    uiState = uiState.copy(
                        canciones = emptyList(),
                        cargando = false,
                        error = result.message
                    )
                }
            }
        }
    }

    fun agregarAFavoritos(cancion: Cancion) {
        viewModelScope.launch {
            when (val result = musicRepository.agregarFavorito(cancion)) {
                is RepositoryResult.Success -> {
                    uiState = uiState.copy(
                        error = "",
                        mensaje = "Agregado a favoritos"
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
