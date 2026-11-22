package com.example.mathrush.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mathrush.game.PracticeReport
import com.example.mathrush.ui.theme.*

@Composable
fun PracticeReportScreen(
    report: PracticeReport,
    onBackHome: () -> Unit
) {
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
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Reporte de Práctica",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MathTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Análisis detallado de tu sesión",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MathTextSecondary
                        )
                    }
                    Button(
                        onClick = onBackHome,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MathPurple),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Finalizar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MathSurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Resumen General", style = MaterialTheme.typography.titleMedium, color = MathCyan, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Preguntas", style = MaterialTheme.typography.labelMedium, color = MathTextSecondary)
                                Text(text = "${report.totalQuestions}", style = MaterialTheme.typography.titleLarge, color = MathTextPrimary, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Aciertos", style = MaterialTheme.typography.labelMedium, color = MathTextSecondary)
                                Text(text = "${report.correctCount} / ${report.totalQuestions}", style = MaterialTheme.typography.titleLarge, color = MathSuccess, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Precisión", style = MaterialTheme.typography.labelMedium, color = MathTextSecondary)
                                Text(text = "${String.format("%.1f", report.accuracyPercentage)}%", style = MaterialTheme.typography.titleLarge, color = MathCyan, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text(text = "Desglose por Categoría", style = MaterialTheme.typography.titleMedium, color = MathTextPrimary, fontWeight = FontWeight.Bold)

                // LazyColumn for categories
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(report.categoryPerformances.values.toList()) { perf ->
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = perf.category.title, style = MaterialTheme.typography.titleSmall, color = MathTextPrimary, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Respondidas: ${perf.totalQuestions} • Correctas: ${perf.correctAnswers}", style = MaterialTheme.typography.bodySmall, color = MathTextSecondary)
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (perf.accuracy >= 70f) MathSuccess.copy(alpha = 0.2f) else MathErrorContainer.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${String.format("%.1f", perf.accuracy)}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (perf.accuracy >= 70f) MathSuccess else MathError,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Recommendations Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "💡 Recomendaciones y Estrategia", style = MaterialTheme.typography.titleSmall, color = MathPurple, fontWeight = FontWeight.Bold)
                        for (rec in report.recommendations) {
                            Text(text = "• $rec", style = MaterialTheme.typography.bodySmall, color = MathTextPrimary)
                        }
                    }
                }
            }
        }
    }
}
