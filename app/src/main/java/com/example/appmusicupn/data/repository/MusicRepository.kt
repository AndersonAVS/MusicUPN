package com.example.appmusicupn.data.repository


import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.model.RadioStation
import com.example.appmusicupn.data.repository.RepositoryResult

interface MusicRepository {
    fun obtenerAlbumesPopulares(): List<Album>

    fun obtenerRadiosPopulares(): List<RadioStation>

    suspend fun crearPlaylist(nombre: String): RepositoryResult<Playlist>

    suspend fun obtenerPlaylistsUsuario(): RepositoryResult<List<Playlist>>

    suspend fun actualizarPlaylist(playlist: Playlist): RepositoryResult<Playlist>

    suspend fun eliminarPlaylist(playlistId: String): RepositoryResult<Unit>

    suspend fun buscarCancionesWeb(query: String): RepositoryResult<List<Cancion>>
}
