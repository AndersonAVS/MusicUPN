package com.example.appmusicupn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ChipBiblioteca(texto: String) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .background(Color(0xFF2A2A2A), RoundedCornerShape(50))
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(texto, color = Color.White)
    }
}

@Composable
fun BibliotecaItem(
    titulo: String,
    descripcion: String,
    icono: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .background(color, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icono, color = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(titulo, color = Color.White, fontWeight = FontWeight.Bold)
            Text(descripcion, color = Color.Gray)
        }
    }
}