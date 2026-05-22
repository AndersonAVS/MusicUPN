package com.example.appmusicupn.data.repository

import com.example.appmusicupn.data.model.Album
import com.example.appmusicupn.data.model.RadioStation

interface MusicRepository {
    fun obtenerAlbumesPopulares(): List<Album>

    fun obtenerRadiosPopulares(): List<RadioStation>
}
