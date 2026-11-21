package com.example.mathrush.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathrush.game.GameViewModel
import com.example.mathrush.ui.theme.*

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    onGameOver: (score: Int) -> Unit,
    onBackToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame(difficultyLevel = 1)
    }

    LaunchedEffect(uiState.isGameOver) {
        if (uiState.isGameOver) {
            onGameOver(uiState.score)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- TOP BAR (Lives, Score, Timer) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToHome) {
                    Text(text = "←", color = MathTextSecondary, style = MaterialTheme.typography.headlineMedium)
                }

                // Vidas (Corazones)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { index ->
                        Text(
                            text = if (index < uiState.lives) "❤️" else "🖤",
                            fontSize = 18.sp
                        )
                    }
                }

                // Puntuación
                Card(
                    colors = CardDefaults.cardColors(containerColor = MathSurfaceCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = " Pts: ${uiState.score} ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MathCyan,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // --- COMBO MULTIPLIER BADGE ---
            if (uiState.comboMultiplier > 1) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            uiState.comboMultiplier >= 5 -> MathPurple
                            uiState.comboMultiplier >= 3 -> MathCyanDark
                            else -> MathSurfaceVariant
                        }
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = " 🔥 COMBO x${uiState.comboMultiplier} (Streak: ${uiState.streak}) ",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(28.dp))
            }

            // --- TIMER BAR ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LinearProgressIndicator(
                    progress = { uiState.timeLeft.toFloat() / uiState.maxTimePerQuestion.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (uiState.timeLeft <= 3) MathError else MathCyan,
                    trackColor = MathSurfaceVariant,
                )
                Text(
                    text = "${uiState.timeLeft}s",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (uiState.timeLeft <= 3) MathError else MathTextSecondary
                )
            }

            // --- MATH QUESTION CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val q = uiState.currentQuestion
                    if (q != null) {
                        Text(
                            text = q.questionText,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MathTextPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        CircularProgressIndicator(color = MathCyan)
                    }
                }
            }

            // --- USER INPUT DISPLAY ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.userInput.ifEmpty { "_" },
                        style = MaterialTheme.typography.headlineLarge,
                        color = if (uiState.userInput.isEmpty()) MathTextSecondary else MathCyan,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- NUMBER KEYPAD ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val buttons = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("⌫", "0", "✓")
                )

                for (row in buttons) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (digit in row) {
                            Button(
                                onClick = {
                                    when (digit) {
                                        "⌫" -> viewModel.onDeleteInput()
                                        "✓" -> viewModel.submitAnswer()
                                        else -> viewModel.onNumberInput(digit)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (digit) {
                                        "✓" -> MathSuccess
                                        "⌫" -> MathErrorContainer
                                        else -> MathSurfaceCard
                                    }
                                )
                            ) {
                                Text(
                                    text = digit,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
