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
import com.example.mathrush.game.PracticeReport
import com.example.mathrush.ui.screens.*
import com.example.mathrush.ui.theme.MathRushTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            MathRushTheme {
                val authRepo = remember { AuthRepository() }
                var pantallaActual by remember { mutableStateOf("splash") }
                var finalScore by remember { mutableIntStateOf(0) }
                var selectedDifficulty by remember { mutableIntStateOf(1) }
                var isPracticeMode by remember { mutableStateOf(false) }
                var currentPracticeReport by remember { mutableStateOf<PracticeReport?>(null) }

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
                                    isPracticeMode = false
                                    pantallaActual = "game"
                                },
                                onPracticeClick = { difficulty ->
                                    selectedDifficulty = difficulty
                                    isPracticeMode = true
                                    pantallaActual = "game"
                                },
                                onLeaderboardClick = { pantallaActual = "leaderboard" },
                                onProfileClick = { pantallaActual = "profile" },
                                onLoginClick = { pantallaActual = "login" },
                                onHistoryClick = { pantallaActual = "history" }
                            )
                            "game" -> GameScreen(
                                difficultyLevel = selectedDifficulty,
                                isPracticeMode = isPracticeMode,
                                onGameOver = { score, report ->
                                    if (isPracticeMode && report != null) {
                                        currentPracticeReport = report
                                        pantallaActual = "practice_report"
                                    } else {
                                        finalScore = score
                                        pantallaActual = "result"
                                    }
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
                            "practice_report" -> {
                                if (currentPracticeReport != null) {
                                    PracticeReportScreen(
                                        report = currentPracticeReport!!,
                                        onBackHome = { pantallaActual = "home" }
                                    )
                                } else {
                                    HomeScreen(
                                        modifier = modifier,
                                        onStartGameClick = { diff -> selectedDifficulty = diff; isPracticeMode = false; pantallaActual = "game" },
                                        onPracticeClick = { diff -> selectedDifficulty = diff; isPracticeMode = true; pantallaActual = "game" },
                                        onLeaderboardClick = { pantallaActual = "leaderboard" },
                                        onProfileClick = { pantallaActual = "profile" },
                                        onLoginClick = { pantallaActual = "login" },
                                        onHistoryClick = { pantallaActual = "history" }
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
