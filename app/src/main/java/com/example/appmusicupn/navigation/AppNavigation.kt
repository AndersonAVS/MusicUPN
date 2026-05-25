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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") { PantallaInicio(navController) }
        composable("login") { PantallaLogin(navController) }
        composable("registrar") { PantallaRegistrar(navController) }
        composable("home") { PantallaHome(navController) }
        composable("buscar") { PantallaBuscar(navController) }
        composable("biblioteca") { PantallaBiblioteca(navController) }
        composable("configuracion_cuenta"){ PantallaConfiguracionCuenta(navController) }
    }
}

