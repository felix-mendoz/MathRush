package com.example.mathrush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mathrush.data.AuthRepository
import com.example.mathrush.ui.screens.*
import com.example.mathrush.ui.theme.MathRushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MathRushTheme {
                val authRepo = remember { AuthRepository() }
                var pantallaActual by remember { mutableStateOf("splash") }
                var finalScore by remember { mutableIntStateOf(0) }
                var selectedDifficulty by remember { mutableIntStateOf(1) }

                if (pantallaActual == "splash") {
                    SplashScreen {
                        pantallaActual = "home"
                    }
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        contentWindowInsets = WindowInsets.safeDrawing
                    ) { innerPadding ->
                        val modifier = Modifier.padding(innerPadding)

                        when (pantallaActual) {
                            "login" -> LoginScreen(
                                onLoginSuccess = { pantallaActual = "home" },
                                onNavigateRegister = { pantallaActual = "register" },
                            )
                            "register" -> RegisterScreen(
                                onRegisterSuccess = { pantallaActual = "home" },
                                onBackToLogin = { pantallaActual = "login" },
                            )
                            "profile" -> ProfileScreen(
                                onNavigateLogin = { pantallaActual = "login" },
                                onBackHome = { pantallaActual = "home" },
                                onSyncComplete = { }
                            )
                            "leaderboard" -> LeaderboardScreen(
                                onBackHome = { pantallaActual = "home" }
                            )
                            "home" -> HomeScreen(
                                modifier = modifier,
                                onStartGameClick = { difficulty ->
                                    selectedDifficulty = difficulty
                                    pantallaActual = "game"
                                },
                                onPracticeClick = {
                                    selectedDifficulty = 1
                                    pantallaActual = "game"
                                },
                                onLeaderboardClick = { pantallaActual = "leaderboard" },
                                onProfileClick = { pantallaActual = "profile" },
                                onLoginClick = { pantallaActual = "login" }
                            )
                            "game" -> GameScreen(
                                difficultyLevel = selectedDifficulty,
                                onGameOver = { score ->
                                    finalScore = score
                                    pantallaActual = "result"
                                },
                                onBackToHome = { pantallaActual = "home" },
                            )
                            "result" -> ResultScreen(
                                score = finalScore,
                                onPlayAgain = { pantallaActual = "game" },
                                onHomeClick = { pantallaActual = "home" },
                            )
                            "history" -> HistoryScreen(
                                onBackToHome = { pantallaActual = "home" },
                            )
                        }
                    }
                }
            }
        }
    }
}
