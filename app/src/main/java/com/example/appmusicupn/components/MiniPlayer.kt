package com.example.appmusicupn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appmusicupn.data.model.Cancion

@Composable
fun MiniPlayer(
    cancionActual: Cancion? = null,
    reproduciendo: Boolean = false,
    onPlayPauseClick: () -> Unit = {}
) {
    val titulo = cancionActual?.titulo ?: "Tan solo tú"
    val artista = cancionActual?.artista ?: "La Llave"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6B5A1E), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .background(Color.DarkGray, RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = artista,
                color = Color.LightGray
            )
        }

        Icon(
            Icons.Default.AddCircle,
            contentDescription = null,
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))

        IconButton(onClick = onPlayPauseClick) {
            Icon(
                imageVector = if (reproduciendo) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = if (reproduciendo) {
                    "Pausar"
                } else {
                    "Reproducir"
                },
                tint = Color.White
            )
        }
    }
}
