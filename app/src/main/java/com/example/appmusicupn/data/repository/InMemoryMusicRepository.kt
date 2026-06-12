package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.model.RadioStation
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.model.Favorito

class InMemoryMusicRepository : MusicRepository {

    private val favoritos = mutableListOf<Favorito>()
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


    override suspend fun crearPlaylist(nombre: String): RepositoryResult<Playlist>
    {
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

    override suspend fun actualizarPlaylist(
        playlist: Playlist
    ): RepositoryResult<Playlist> {
        val index = playlists.indexOfFirst { it.id == playlist.id }

        if (index == -1) {
            return RepositoryResult.Error("No se encontró la playlist")
        }

        playlists[index] = playlist

        return RepositoryResult.Success(playlist)
    }

    override suspend fun eliminarPlaylist(
        playlistId: String
    ): RepositoryResult<Unit> {
        val eliminada = playlists.removeIf { it.id == playlistId }

        return if (eliminada) {
            RepositoryResult.Success(Unit)
        } else {
            RepositoryResult.Error("No se encontró la playlist")
        }
    }

    override suspend fun buscarCancionesWeb(
        query: String
    ): RepositoryResult<List<Cancion>> {
        val queryLimpio = query.trim()

        if (queryLimpio.isBlank()) {
            return RepositoryResult.Error("Ingresa una búsqueda")
        }

        val cancionesMock = listOf(
            Cancion(
                id = "mock-1",
                titulo = "Canción de prueba",
                artista = "MusicUPN",
                album = "Demo",
                audioUrl = "",
                portadaUrl = "",
                origen = "mock"
            ),
            Cancion(
                id = "mock-2",
                titulo = "Rock libre",
                artista = "Artista demo",
                album = "Jamendo Demo",
                audioUrl = "",
                portadaUrl = "",
                origen = "mock"
            ),
            Cancion(
                id = "mock-3",
                titulo = "Piano instrumental",
                artista = "Demo Artist",
                album = "Instrumental",
                audioUrl = "",
                portadaUrl = "",
                origen = "mock"
            )
        )

        val resultados = cancionesMock.filter { cancion ->
            cancion.titulo.contains(queryLimpio, ignoreCase = true) ||
                cancion.artista.contains(queryLimpio, ignoreCase = true) ||
                cancion.album.contains(queryLimpio, ignoreCase = true)
        }

        return RepositoryResult.Success(resultados)
    }

    override suspend fun agregarFavorito(
        cancion: Cancion
    ): RepositoryResult<Favorito> {
        if (cancion.id.isBlank()) {
            return RepositoryResult.Error("Canción inválida")
        }

        val favorito = Favorito(
            id = cancion.id,
            titulo = cancion.titulo,
            artista = cancion.artista,
            album = cancion.album,
            audioUrl = cancion.audioUrl,
            portadaUrl = cancion.portadaUrl,
            origen = cancion.origen,
            fechaAgregado = System.currentTimeMillis()
        )

        favoritos.removeAll { it.id == favorito.id }
        favoritos.add(favorito)

        return RepositoryResult.Success(favorito)
    }
}
