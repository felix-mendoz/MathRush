package com.example.mathrush.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathrush.data.AuthRepository
import com.example.mathrush.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onStartGameClick: (difficulty: Int) -> Unit,
    onPracticeClick: (difficulty: Int) -> Unit,
    onLeaderboardClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    val authRepo = remember { AuthRepository() }
    val currentUser = authRepo.currentUser
    var selectedDifficulty by remember { mutableStateOf(1) } // 1: Fácil, 2: Intermedio, 3: Avanzado

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (currentUser != null) onProfileClick() else onLoginClick()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MathPurple),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (currentUser != null) "Mi Perfil (${authRepo.currentUserName})" else "Iniciar Sesión",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }

            // --- LOGO & TITLE ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Text(
                    text = "MATH RUSH",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MathTextPrimary,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- DIFFICULTY SELECTOR & TOOLTIP ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Selecciona la Dificultad",
                    style = MaterialTheme.typography.titleMedium,
                    color = MathTextSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val difficulties = listOf(
                        Triple(1, "Fácil", "15s • 1.0x"),
                        Triple(2, "Intermedio", "10s • 1.5x"),
                        Triple(3, "Avanzado", "20s • 2.0x")
                    )

                    for ((level, title, desc) in difficulties) {
                        val isSelected = selectedDifficulty == level
                        Card(
                            onClick = { selectedDifficulty = level },
                            modifier = Modifier
                                .weight(1f)
                                .height(68.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MathPurple else MathSurfaceCard
                            ),
                            border = if (isSelected) BorderStroke(2.dp, MathCyan) else null
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isSelected) Color.White else MathTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else MathTextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
                ) {
                    val tooltipText = when (selectedDifficulty) {
                        1 -> "Fácil: 20s por pregunta • Aritmética básica y geometría elemental • Multiplicador: 1.0x"
                        2 -> "Intermedio: 15s por pregunta • Aritmética, Álgebra y Pitágoras • Multiplicador: 1.5x"
                        else -> "Avanzado: 25s por pregunta • Álgebra, Cálculo integral y Trigonometría avanzada • Multiplicador: 2.0x"
                    }
                    Text(
                        text = tooltipText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MathCyan,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- ACTION BUTTONS ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onStartGameClick(selectedDifficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Iniciar Modo Desafío",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onPracticeClick(selectedDifficulty) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF34D399), Color(0xFF059669))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Modo Práctica",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onLeaderboardClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Clasificación",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = onHistoryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MathSurfaceCard)
                ) {
                    Text(
                        text = "Historial y Récords Locales",
                        style = MaterialTheme.typography.titleSmall,
                        color = MathCyan,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
