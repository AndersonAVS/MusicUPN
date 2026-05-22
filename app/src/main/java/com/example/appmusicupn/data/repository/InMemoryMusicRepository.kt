package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.RadioStation

class InMemoryMusicRepository : MusicRepository {
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
}
