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
fun CategoriaCard(titulo: String, color: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp)
            .padding(end = 16.dp)
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text = titulo,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp)
        )
    }
}