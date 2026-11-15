package com.example.mathrush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mathrush.ui.screens.*
import com.example.mathrush.ui.theme.MathRushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathRushTheme {
                var pantallaActual by remember { mutableStateOf("login") }
                var finalScore by remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    val modifier = Modifier.padding(innerPadding)

                    when (pantallaActual) {
                        "login" -> LoginScreen(
                            onLoginSuccess = { pantallaActual = "home" },
                            onNavigateRegister = { pantallaActual = "register" }
                        )
                        "register" -> RegisterScreen(
                            onRegisterSuccess = { pantallaActual = "home" },
                            onBackToLogin = { pantallaActual = "login" }
                        )
                        "home" -> HomeScreen(
                            modifier = modifier,
                            onStartGameClick = { pantallaActual = "game" },
                            onPracticeClick = { pantallaActual = "game" },
                            onLeaderboardClick = { pantallaActual = "history" },
                            onHelpClick = { /* Opcional: Ayuda */ },
                            onMenuClick = { pantallaActual = "login" }
                        )
                        "game" -> GameScreen(
                            onGameOver = { score ->
                                finalScore = score
                                pantallaActual = "result"
                            },
                            onBackToHome = { pantallaActual = "home" }
                        )
                        "result" -> ResultScreen(
                            score = finalScore,
                            onPlayAgain = { pantallaActual = "game" },
                            onHomeClick = { pantallaActual = "home" }
                        )
                        "history" -> HistoryScreen(
                            onBackToHome = { pantallaActual = "home" }
                        )
                    }
                }
            }
        }
    }
}
