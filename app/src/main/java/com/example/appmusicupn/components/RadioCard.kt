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
fun RadioCard(titulo: String, descripcion: String) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
                .background(Color(0xFF8BC34A), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(titulo, color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(descripcion, color = Color.Gray)
    }
}