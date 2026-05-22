package com.example.appmusicupn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.RadioStation
import com.example.appmusicupn.data.repository.MusicRepository
import com.example.appmusicupn.data.repository.RepositoryProvider

data class HomeUiState(
    val albumes: List<Album> = emptyList(),
    val radios: List<RadioStation> = emptyList()
)

class HomeViewModel(
    private val musicRepository: MusicRepository = RepositoryProvider.musicRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        cargarContenido()
    }

    private fun cargarContenido() {
        uiState = HomeUiState(
            albumes = musicRepository.obtenerAlbumesPopulares(),
            radios = musicRepository.obtenerRadiosPopulares()
        )
    }
}
