package com.example.mathrush.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mathrush.ui.theme.*

@Composable
fun HistoryScreen(
    onBackToHome: () -> Unit,
) {
    val sampleScores = listOf(
        Pair("Partida Rápida", 420),
        Pair("Modo Práctica", 310),
        Pair("Desafío Diario", 280),
        Pair("Partida Rápida", 190)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToHome) {
                    Text(text = "←", color = MathTextSecondary, style = MaterialTheme.typography.headlineMedium)
                }
                Text(
                    text = "Historial y Récords",
                    style = MaterialTheme.typography.titleLarge,
                    color = MathTextPrimary
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleScores.size) { index ->
                    val item = sampleScores[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.first,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MathTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Hace ${index + 1} día(s)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MathTextSecondary
                                )
                            }
                            Text(
                                text = "${item.second} Pts",
                                style = MaterialTheme.typography.titleLarge,
                                color = MathCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MathPurple)
            ) {
                Text(
                    text = "Volver al Inicio",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(
        onBackToHome = {}
    )
}
