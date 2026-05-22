package com.example.appmusicupn.ui.buscar

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.CategoriaCard
import com.example.appmusicupn.components.MiniPlayer

@Composable
fun PantallaBuscar(navController: NavController) {
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
                text = "Buscar",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "¿Qué quieres escuchar?",
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Descubre algo nuevo para ti",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            CategoriaCard("#reggaeton", Color(0xFF6D1B1B))
            CategoriaCard("#urbano latino", Color(0xFF1E3A8A))
            CategoriaCard("#sunrise", Color(0xFFB45309))
        }

        Spacer(modifier = Modifier.weight(1f))

        MiniPlayer()

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(navController, "buscar")
    }
}