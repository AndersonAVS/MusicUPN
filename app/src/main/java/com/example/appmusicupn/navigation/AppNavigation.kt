package com.example.appmusicupn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmusicupn.ui.biblioteca.PantallaBiblioteca
import com.example.appmusicupn.ui.buscar.PantallaBuscar
import com.example.appmusicupn.ui.cuenta.PantallaConfiguracionCuenta
import com.example.appmusicupn.ui.home.PantallaHome
import com.example.appmusicupn.ui.inicio.PantallaInicio
import com.example.appmusicupn.ui.login.PantallaLogin
import com.example.appmusicupn.ui.registrar.PantallaRegistrar
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appmusicupn.viewmodel.PlaybackViewModel
import com.example.appmusicupn.ui.favoritos.PantallaFavoritos
import com.example.appmusicupn.data.model.Playlist
import com.example.appmusicupn.ui.playlist.PantallaDetallePlaylist

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val playbackViewModel: PlaybackViewModel = viewModel()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") { PantallaInicio(navController) }
        composable("login") { PantallaLogin(navController) }
        composable("registrar") { PantallaRegistrar(navController) }
        composable("home") { PantallaHome(navController=navController, playbackViewModel = playbackViewModel) }
        composable("buscar") { PantallaBuscar(navController=navController, playbackViewModel = playbackViewModel) }
        composable("biblioteca") { PantallaBiblioteca(navController=navController, playbackViewModel = playbackViewModel) }
        composable("favoritos") { PantallaFavoritos(navController=navController, playbackViewModel=playbackViewModel) }
        composable("detalle_playlist") {
            val playlist = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<Playlist>("playlistSeleccionada")

            if (playlist != null) {
                PantallaDetallePlaylist(
                    navController = navController,
                    playbackViewModel = playbackViewModel,
                    playlist = playlist
                )
            }
        }
        composable("configuracion_cuenta"){ PantallaConfiguracionCuenta(navController) }
    }
}

