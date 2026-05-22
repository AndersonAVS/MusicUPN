package com.example.appmusicupn.data.repository

object RepositoryProvider {
    val authRepository: AuthRepository = InMemoryAuthRepository()
    val musicRepository: MusicRepository = InMemoryMusicRepository()
}
