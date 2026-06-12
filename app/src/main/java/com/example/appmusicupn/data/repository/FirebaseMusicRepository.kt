package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.data.model.RadioStation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.appmusicupn.data.model.Cancion
import com.example.appmusicupn.data.remote.JamendoApiClient
import com.example.appmusicupn.data.model.Favorito

class FirebaseMusicRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),

) : MusicRepository {

    private val jamendoClientId = "52c40038"

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
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        val nombreLimpio = nombre.trim()

        if (nombreLimpio.isBlank()) {
            return RepositoryResult.Error("Ingresa un nombre para la playlist")
        }

        return try {
            val playlistRef = firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("playlists")
                .document()

            val playlist = Playlist(
                id = playlistRef.id,
                nombre = nombreLimpio,
                descripcion = "Playlist creada por ti",
                fechaCreacion = System.currentTimeMillis(),
                cancionesIds = emptyList()
            )

            playlistRef.set(playlist).await()

            RepositoryResult.Success(playlist)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudo crear la playlist")
        }
    }

    override suspend fun obtenerPlaylistsUsuario(): RepositoryResult<List<Playlist>> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        return try {
            val snapshot = firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("playlists")
                .get()
                .await()

            val playlists = snapshot.documents.mapNotNull { document ->
                document.toObject(Playlist::class.java)
            }

            RepositoryResult.Success(playlists)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudieron cargar tus playlists")
        }
    }

    override suspend fun actualizarPlaylist(
        playlist: Playlist
    ): RepositoryResult<Playlist> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        if (playlist.id.isBlank()) {
            return RepositoryResult.Error("Playlist inválida")
        }

        if (playlist.nombre.trim().isBlank()) {
            return RepositoryResult.Error("El nombre no puede estar vacío")
        }

        return try {
            firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("playlists")
                .document(playlist.id)
                .set(playlist)
                .await()

            RepositoryResult.Success(playlist)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudo actualizar la playlist")
        }
    }

    override suspend fun eliminarPlaylist(
        playlistId: String
    ): RepositoryResult<Unit> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        if (playlistId.isBlank()) {
            return RepositoryResult.Error("Playlist inválida")
        }

        return try {
            firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("playlists")
                .document(playlistId)
                .delete()
                .await()

            RepositoryResult.Success(Unit)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudo eliminar la playlist")
        }
    }

    override suspend fun buscarCancionesWeb(
        query: String
    ): RepositoryResult<List<Cancion>> {
        val queryLimpio = query.trim()

        if (queryLimpio.isBlank()) {
            return RepositoryResult.Error("Ingresa una búsqueda")
        }

        return try {
            val response = JamendoApiClient.service.buscarTracks(
                clientId = jamendoClientId,
                search = queryLimpio
            )

            val canciones = response.results.map { track ->
                Cancion(
                    id = track.id,
                    titulo = track.name,
                    artista = track.artist_name,
                    album = track.album_name,
                    audioUrl = track.audio,
                    portadaUrl = track.album_image,
                    origen = "jamendo"
                )
            }

            RepositoryResult.Success(canciones)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudieron buscar canciones")
        }
    }

    override suspend fun agregarFavorito(
        cancion: Cancion
    ): RepositoryResult<Favorito> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        if (cancion.id.isBlank()) {
            return RepositoryResult.Error("Canción inválida")
        }

        return try {
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

            firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("favoritos")
                .document(cancion.id)
                .set(favorito)
                .await()

            RepositoryResult.Success(favorito)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudo agregar a favoritos")
        }
    }

    override suspend fun obtenerFavoritosUsuario(): RepositoryResult<List<Favorito>> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        return try {
            val snapshot = firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("favoritos")
                .get()
                .await()

            val favoritos = snapshot.documents.mapNotNull { document ->
                document.toObject(Favorito::class.java)
            }.sortedByDescending { favorito ->
                favorito.fechaAgregado
            }

            RepositoryResult.Success(favoritos)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudieron cargar tus favoritos")
        }
    }

    override suspend fun eliminarFavorito(
        favoritoId: String
    ): RepositoryResult<Unit> {
        val usuario = firebaseAuth.currentUser
            ?: return RepositoryResult.Error("No hay una sesión activa")

        if (favoritoId.isBlank()) {
            return RepositoryResult.Error("Favorito inválido")
        }

        return try {
            firestore
                .collection("usuarios")
                .document(usuario.uid)
                .collection("favoritos")
                .document(favoritoId)
                .delete()
                .await()

            RepositoryResult.Success(Unit)
        } catch (exception: Exception) {
            RepositoryResult.Error("No se pudo quitar de favoritos")
        }
    }
}
