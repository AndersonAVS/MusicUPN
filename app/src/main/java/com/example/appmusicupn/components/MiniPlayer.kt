package com.example.appmusicupn.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MiniPlayer() {
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
            Text("Tan solo tú", color = Color.White, fontWeight = FontWeight.Bold)
            Text("La Llave", color = Color.LightGray)
        }

        Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
    }
}