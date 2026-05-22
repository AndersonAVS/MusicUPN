package com.example.appmusicupn.ui.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmusicupn.components.BibliotecaItem
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.ChipBiblioteca
import com.example.appmusicupn.components.MiniPlayer
import com.example.appmusicupn.models.BibliotecaItemModel

@Composable
fun PantallaBiblioteca(navController: NavController) {
    val items = listOf(
        BibliotecaItemModel("Tus me gusta", "Playlist • 1605 canciones", "❤", Color(0xFF7C3AED)),
        BibliotecaItemModel("Tus episodios", "Playlist • Episodios guardados", "🔖", Color(0xFF047857)),
        BibliotecaItemModel("Nuevos episodios", "Se actualizó ayer", "🔔", Color(0xFF6D28D9)),
        BibliotecaItemModel("WORKOUT MUSIC 2026", "Playlist • Magic Records", "⚡", Color(0xFF374151)),
        BibliotecaItemModel("Música Andina", "Playlist • Anderson Vargas Salcedo", "🎵", Color(0xFF92400E))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "A",
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0xFFB39DDB), RoundedCornerShape(50))
                    .padding(14.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Tu biblioteca",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            ChipBiblioteca("Playlists")
            ChipBiblioteca("Podcasts")
            ChipBiblioteca("Álbumes")
            ChipBiblioteca("Artistas")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "↕  Recientes",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        items.forEach { item ->
            BibliotecaItem(
                titulo = item.titulo,
                descripcion = item.descripcion,
                icono = item.icono,
                color = item.color
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        MiniPlayer()

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(navController, "biblioteca")
    }
}