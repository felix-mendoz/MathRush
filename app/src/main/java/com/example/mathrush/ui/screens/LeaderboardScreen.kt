package com.example.mathrush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathrush.data.FirestoreRepository
import com.example.mathrush.data.LeaderboardItem
import com.example.mathrush.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LeaderboardScreen(
    onBackHome: () -> Unit
) {
    val firestoreRepo = remember { FirestoreRepository() }
    val coroutineScope = rememberCoroutineScope()
    var leaderboardItems by remember { mutableStateOf<List<LeaderboardItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isLoading = true
            val result = firestoreRepo.getGlobalLeaderboard()
            if (result.isSuccess) {
                leaderboardItems = result.getOrDefault(emptyList())
                errorMessage = null
            } else {
                errorMessage = "No se pudo conectar a Firebase o no hay conexión a Internet."
            }
            isLoading = false
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
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Clasificación Global",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MathTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Mejores puntajes de la comunidad",
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
                    Text("Volver", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MathPurple)
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = errorMessage!!, color = MathTextSecondary, style = MaterialTheme.typography.bodyMedium)
                            Button(onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    val result = firestoreRepo.getGlobalLeaderboard()
                                    if (result.isSuccess) {
                                        leaderboardItems = result.getOrDefault(emptyList())
                                        errorMessage = null
                                    } else {
                                        errorMessage = "Error de conexión."
                                    }
                                    isLoading = false
                                }
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                leaderboardItems.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay puntuaciones registradas aún.", color = MathTextSecondary)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(leaderboardItems) { index, item ->
                            val rankColor = when (index) {
                                0 -> Color(0xFFFFD700) // Oro
                                1 -> Color(0xFFC0C0C0) // Plata
                                2 -> Color(0xFFCD7F32) // Bronce
                                else -> MathSurfaceVariant
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (index < 3) 4.dp else 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = rankColor.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "#${index + 1}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = rankColor,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = item.username,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MathTextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = item.category.ifEmpty { "Matemáticas" },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MathTextSecondary
                                            )
                                        }
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${item.score} pts",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MathCyan,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Combo x${item.maxCombo}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MathTextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
