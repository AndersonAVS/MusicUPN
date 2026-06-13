package com.example.appmusicupn.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.appmusicupn.data.model.Cancion

data class PlaybackUiState(
    val cancionActual: Cancion? = null,
    val cola: List<Cancion> = emptyList(),
    val indiceActual: Int = -1,
    val reproduciendo: Boolean = false,
    val preparado: Boolean = false,
    val error: String = ""
)

class PlaybackViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val player: ExoPlayer = ExoPlayer.Builder(application).build()

    var uiState by mutableStateOf(PlaybackUiState())
        private set

    init {
        player.addListener(
            object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        siguienteCancion()
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    uiState = uiState.copy(
                        reproduciendo = isPlaying
                    )
                }
            }
        )
    }

    fun reproducirCancion(cancion: Cancion) {
        reproducirLista(
            canciones = listOf(cancion),
            indiceInicial = 0
        )
    }

    fun reproducirLista(
        canciones: List<Cancion>,
        indiceInicial: Int = 0
    ) {
        if (canciones.isEmpty()) {
            uiState = uiState.copy(
                error = "No hay canciones para reproducir"
            )
            return
        }

        if (indiceInicial !in canciones.indices) {
            uiState = uiState.copy(
                error = "Canción inválida"
            )
            return
        }

        val cancion = canciones[indiceInicial]

        if (cancion.audioUrl.isBlank()) {
            uiState = uiState.copy(
                error = "La canción no tiene una URL de audio"
            )
            return
        }

        val mediaItem = MediaItem.fromUri(cancion.audioUrl)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        uiState = uiState.copy(
            cancionActual = cancion,
            cola = canciones,
            indiceActual = indiceInicial,
            reproduciendo = true,
            preparado = true,
            error = ""
        )
    }

    fun alternarPlayPause() {
        val cancion = uiState.cancionActual ?: return

        if (!uiState.preparado) {
            reproducirCancion(cancion)
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

    fun siguienteCancion() {
        val colaActual = uiState.cola
        val siguienteIndice = uiState.indiceActual + 1

        if (colaActual.isEmpty() || siguienteIndice !in colaActual.indices) {
            detenerReproduccion()
            return
        }

        reproducirLista(
            canciones = colaActual,
            indiceInicial = siguienteIndice
        )
    }

    fun cancionAnterior() {
        val colaActual = uiState.cola
        val indiceAnterior = uiState.indiceActual - 1

        if (colaActual.isEmpty() || indiceAnterior !in colaActual.indices) {
            return
        }

        reproducirLista(
            canciones = colaActual,
            indiceInicial = indiceAnterior
        )
    }

    fun detenerReproduccion() {
        player.stop()

        uiState = uiState.copy(
            reproduciendo = false,
            preparado = false
        )
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}
