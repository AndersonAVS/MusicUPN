package com.example.appmusicupn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearBottomSheet(
    mostrar: Boolean,
    onCerrar: () -> Unit,
    onCrearPlaylist: () -> Unit
) {
    if (mostrar) {
        ModalBottomSheet(
            onDismissRequest = { onCerrar() },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color(0xFF121212),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.Gray, RoundedCornerShape(50))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                OpcionCrear(
                    icono = "♫",
                    titulo = "Playlist",
                    descripcion = "Crea una playlist con canciones o episodios",
                    onClick = onCrearPlaylist
                )

                Spacer(modifier = Modifier.height(24.dp))

                OpcionCrear(
                    icono = "👥",
                    titulo = "Playlist colaborativa",
                    descripcion = "Crea una playlist con tus personas favoritas"
                )

                Spacer(modifier = Modifier.height(24.dp))

                OpcionCrear(
                    icono = "●●",
                    titulo = "Fusión",
                    descripcion = "Combina los gustos de tus personas favoritas"
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun OpcionCrear(
    icono: String,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFF2A2A2A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icono, color = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = descripcion,
                color = Color.Gray
            )
        }
    }
}
