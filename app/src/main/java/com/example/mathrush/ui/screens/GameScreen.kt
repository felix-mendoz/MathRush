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
import com.example.mathrush.ui.components.MathView
import com.example.mathrush.ui.theme.*

@Composable
fun GameScreen(
    difficultyLevel: Int = 1,
    viewModel: GameViewModel = viewModel(),
    onGameOver: (score: Int) -> Unit,
    onBackToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startGame(difficultyLevel = difficultyLevel)
    }

    LaunchedEffect(uiState.isGameOver) {
        if (uiState.isGameOver) {
            onGameOver(uiState.score)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
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
                    .height(210.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val q = uiState.currentQuestion
                    if (q != null) {
                        // Category & Subcategory chip
                        Text(
                            text = "${q.category.title} • ${q.subcategory}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MathCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = q.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MathTextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (q.latexExpression != null) {
                            MathView(
                                latex = q.latexExpression,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                displayMode = true
                            )
                        }
                    } else {
                        CircularProgressIndicator(color = MathCyan)
                    }
                }
            }

            // --- USER INPUT DISPLAY ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.userInput.ifEmpty { "Escribe tu respuesta..." },
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (uiState.userInput.isEmpty()) MathTextSecondary else MathCyan,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- EXTENDED NUMBER KEYPAD (Digits, -, ., /, ⌫, ✓) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttons = listOf(
                    listOf("1", "2", "3", "-"),
                    listOf("4", "5", "6", "."),
                    listOf("7", "8", "9", "/"),
                    listOf("⌫", "0", "✓")
                )

                for (row in buttons) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (key in row) {
                            Button(
                                onClick = {
                                    when (key) {
                                        "⌫" -> viewModel.onDeleteInput()
                                        "✓" -> viewModel.submitAnswer()
                                        else -> viewModel.onNumberInput(key)
                                    }
                                },
                                modifier = Modifier
                                    .weight(if (row.size == 4) 1f else if (key == "0" || key == "⌫" || key == "✓") 1.33f else 1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (key) {
                                        "✓" -> MathSuccess
                                        "⌫" -> MathErrorContainer
                                        else -> MathSurfaceCard
                                    }
                                )
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.titleMedium,
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
