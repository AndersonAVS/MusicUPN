package com.example.appmusicupn.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmusicupn.components.AlbumCard
import com.example.appmusicupn.components.BottomMenu
import com.example.appmusicupn.components.MiniPlayer
import com.example.appmusicupn.components.RadioCard
import com.example.appmusicupn.viewmodel.HomeViewModel

@Composable
fun PantallaHome(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState = homeViewModel.uiState

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

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = { }) {
                Text("Todas")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(onClick = { }) {
                Text("Música", color = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedButton(onClick = { }) {
                Text("Podcasts", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Álbumes y sencillos populares",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                uiState.albumes.forEach { album ->
                    AlbumCard(album.titulo, album.artista)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Estaciones populares",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                uiState.radios.forEach { radio ->
                    RadioCard(radio.titulo, radio.descripcion)
                }
            }
        }

        MiniPlayer()

        Spacer(modifier = Modifier.height(12.dp))

        BottomMenu(navController, "home")
    }
}
