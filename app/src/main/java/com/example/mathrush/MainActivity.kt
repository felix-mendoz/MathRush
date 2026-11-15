package com.example.mathrush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mathrush.ui.screens.HomeScreen
import com.example.mathrush.ui.theme.MathRushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathRushTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    HomeScreen(
                        onStartGameClick = { /* TODO */ },
                        onPracticeClick = { /* TODO */ },
                        onLeaderboardClick = { /* TODO */ },
                        onHelpClick = { /* TODO */ },
                        onMenuClick = { /* TODO */ },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
