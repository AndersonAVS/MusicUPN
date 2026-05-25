package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.model.RadioStation

class InMemoryMusicRepository : MusicRepository {

    private val playlists = mutableListOf<Playlist>()

    override fun obtenerAlbumesPopulares(): List<Album> = listOf(
        Album(id = "album-1", titulo = "Homerun", artista = "Paulo Londra"),
        Album(id = "album-2", titulo = "Un Verano Sin Ti", artista = "Bad Bunny"),
        Album(id = "album-3", titulo = "Oasis", artista = "J Balvin")
    )

    override fun obtenerRadiosPopulares(): List<RadioStation> = listOf(
        RadioStation(id = "radio-1", titulo = "Maná", descripcion = "Maná, La Oreja de Van Gogh..."),
        RadioStation(id = "radio-2", titulo = "Corazón Serrano", descripcion = "Corazón Serrano, Papillón..."),
        RadioStation(id = "radio-3", titulo = "Don Omar", descripcion = "Don Omar, Zion...")
    )


    override suspend fun crearPlaylist(nombre: String): RepositoryResult<Playlist> {
        val nombreLimpio = nombre.trim()

    if (nombreLimpio.isBlank()) {
        return RepositoryResult.Error("Ingresa un nombre para la playlist")
    }

    val playlist = Playlist(
        id = "playlist-${playlists.size + 1}",
        nombre = nombreLimpio,
        descripcion = "Playlist creada por ti",
        fechaCreacion = System.currentTimeMillis(),
        cancionesIds = emptyList()
    )

    playlists.add(playlist)

    return RepositoryResult.Success(playlist)
}

override suspend fun obtenerPlaylistsUsuario(): RepositoryResult<List<Playlist>> {
    return RepositoryResult.Success(playlists)
}
}
