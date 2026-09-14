package com.example.mrd.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RestaurantImage(
    name: String,
    seed: Int,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp
) {
    val palette = listOf(
        Color(0xFF0F766E) to Color(0xFF14B8A6),
        Color(0xFF9F1239) to Color(0xFFFB7185),
        Color(0xFF4338CA) to Color(0xFF818CF8),
        Color(0xFF854D0E) to Color(0xFFF59E0B),
        Color(0xFF166534) to Color(0xFF22C55E)
    )
    val colors = palette[kotlin.math.abs(seed) % palette.size]

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(listOf(colors.first, colors.second))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(1),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
