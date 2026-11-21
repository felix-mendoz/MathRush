package com.example.mathrush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathrush.data.AuthRepository
import com.example.mathrush.data.SyncManager
import com.example.mathrush.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateLogin: () -> Unit,
    onBackHome: () -> Unit,
    onSyncComplete: () -> Unit
) {
    val authRepo = remember { AuthRepository() }
    val currentUser = authRepo.currentUser
    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    val syncManager = remember { SyncManager(context) }
    var syncStatus by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(MathPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentUser != null) currentUser.email?.take(2)?.uppercase() ?: "MR" else "OF",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (currentUser != null) {
                        Text(
                            text = currentUser.email ?: "Usuario Autenticado",
                            style = MaterialTheme.typography.titleMedium,
                            color = MathTextPrimary
                        )
                        Text(
                            text = "Modo Online (Sincronización activa)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MathCyan
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isSyncing = true
                                    val success = syncManager.syncPendingScores()
                                    isSyncing = false
                                    syncStatus = if (success) "¡Sincronización exitosa!" else "Error al sincronizar"
                                    if (success) onSyncComplete()
                                }
                            },
                            enabled = !isSyncing,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MathCyan)
                        ) {
                            Text(text = if (isSyncing) "Sincronizando..." else "Sincronizar Partidas Pendientes", color = Color.Black)
                        }

                        if (syncStatus != null) {
                            Text(text = syncStatus!!, style = MaterialTheme.typography.bodySmall, color = MathTextSecondary)
                        }

                        OutlinedButton(
                            onClick = {
                                authRepo.signOut()
                                onNavigateLogin()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Cerrar Sesión")
                        }
                    } else {
                        Text(
                            text = "Modo Offline (Predeterminado)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MathTextPrimary
                        )
                        Text(
                            text = "Tus partidas se guardan localmente en SQLite. Inicia sesión para respaldar en la nube y competir en la clasificación global.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MathTextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onNavigateLogin,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MathPurple)
                        ) {
                            Text("Iniciar Sesión / Registrarse")
                        }
                    }

                    TextButton(onClick = onBackHome) {
                        Text("Volver al Inicio", color = MathTextSecondary)
                    }
                }
            }
        }
    }
}
