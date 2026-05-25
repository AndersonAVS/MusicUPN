package com.example.appmusicupn.data.repository

object RepositoryProvider {
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val musicRepository: MusicRepository = InMemoryMusicRepository()
}
