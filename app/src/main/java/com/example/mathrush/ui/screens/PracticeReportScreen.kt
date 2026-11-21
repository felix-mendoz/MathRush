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
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reporte de Práctica",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MathTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onBackHome) {
                    Text("Finalizar", color = MathCyan)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Resumen General", style = MaterialTheme.typography.titleMedium, color = MathPurple, fontWeight = FontWeight.Bold)
                    Text(text = "Preguntas totales: ${report.totalQuestions}", color = MathTextPrimary)
                    Text(text = "Correctas: ${report.correctCount} | Incorrectas: ${report.incorrectCount}", color = MathTextPrimary)
                    Text(text = "Precisión: ${String.format("%.1f", report.accuracyPercentage)}%", color = MathCyan, fontWeight = FontWeight.Bold)
                    Text(text = "Tiempo promedio: ${String.format("%.1f", report.averageTimeSeconds)}s", color = MathTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Rendimiento por Categoría", style = MaterialTheme.typography.titleMedium, color = MathTextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(report.categoryPerformances.values.toList()) { perf ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = perf.category.title, style = MaterialTheme.typography.bodyLarge, color = MathTextPrimary, fontWeight = FontWeight.Bold)
                                Text(text = "Respondidas: ${perf.totalQuestions} (Correctas: ${perf.correctAnswers})", style = MaterialTheme.typography.bodySmall, color = MathTextSecondary)
                            }
                            Text(text = "${String.format("%.1f", perf.accuracy)}%", style = MaterialTheme.typography.titleMedium, color = if (perf.accuracy >= 70f) MathCyan else Color(0xFFFF6B6B), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Recomendaciones Inteligentes", style = MaterialTheme.typography.titleMedium, color = MathPurple, fontWeight = FontWeight.Bold)
                    for (rec in report.recommendations) {
                        Text(text = "• $rec", style = MaterialTheme.typography.bodyMedium, color = MathTextPrimary)
                    }
                }
            }
        }
    }
}
