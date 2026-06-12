package com.example.appmusicupn.viewmodel

import android.app.Application
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.appmusicupn.data.model.Cancion

data class PlaybackUiState(
    val cancionActual: Cancion? = null,
    val reproduciendo: Boolean = false,
    val error: String = ""
)

class PlaybackViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val player: ExoPlayer = ExoPlayer.Builder(application).build()

    var uiState by mutableStateOf(PlaybackUiState())
        private set

    fun reproducirCancion(cancion: Cancion) {
        if (cancion.audioUrl.isBlank()) {
            uiState = uiState.copy(error = "La canción no tiene una URL de audio")
            return
        }

        val mediaItem = MediaItem.fromUri(cancion.audioUrl)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        uiState = uiState.copy(
            cancionActual = cancion,
            reproduciendo = true,
            error = ""
        )
    }

    fun alternarPlayPause() {
        if (uiState.cancionActual == null) {
            return
        }

        if (player.isPlaying) {
            player.pause()
            uiState = uiState.copy(reproduciendo = false)
        } else {
            player.play()
            uiState = uiState.copy(reproduciendo = true)
        }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}
