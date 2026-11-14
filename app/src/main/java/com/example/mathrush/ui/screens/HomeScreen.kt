package com.example.mathrush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathrush.ui.theme.*

@Composable
fun HomeScreen(
    onStartGameClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onHelpClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onHelpClick) {
                    Icon(
                        imageVector = Icons.Outlined.HelpOutline,
                        contentDescription = "Ayuda",
                        tint = MathTextSecondary
                    )
                }
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = MathTextSecondary
                    )
                }
            }

            // --- LOGO & TITLE ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Icono decorativo estilo arcade (puedes reemplazarlo por una imagen/vector propio)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MathPurple, MathCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+ ÷\n× =",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp
                    )
                }

                Text(
                    text = "MATHRUSH",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MathTextPrimary,
                    letterSpacing = 2.sp
                )
            }

            // --- PLAYER PROFILE & STATS CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Kyle Walker 001",
                        style = MaterialTheme.typography.titleLarge,
                        color = MathTextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(title = "Daily Ranking", value = "6 / 155")
                        StatItem(title = "Weekly Ranking", value = "14 / 798")
                        StatItem(title = "Global Ranking", value = "32 / 2798")
                    }
                }
            }

            // --- ACTION BUTTONS ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Principal (Online Game / Partida Rápida)
                Button(
                    onClick = onStartGameClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MathPurple
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Modo Juego",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "¡Desafía tu mente contra el tiempo!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MathTextSecondary
                        )
                    }
                }

                // Botones secundarios (Práctica y Récords/Leaderboard)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onPracticeClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MathSuccessContainer
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Practice",
                                style = MaterialTheme.typography.titleMedium,
                                color = MathSuccess,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Sin límite",
                                style = MaterialTheme.typography.labelMedium,
                                color = MathTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = onLeaderboardClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MathSurfaceVariant
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Leaderboard",
                                style = MaterialTheme.typography.titleMedium,
                                color = MathCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ver récords",
                                style = MaterialTheme.typography.labelMedium,
                                color = MathTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MathTextSecondary,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MathCyan,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen()
{
    HomeScreen(
        {},
        {},
        {},
        {},
        {}
    )
}