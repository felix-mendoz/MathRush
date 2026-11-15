package com.example.mathrush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathrush.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateRegister: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(MathPurple, MathCyan)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+ ÷\n× =",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "MATH RUSH",
                style = MaterialTheme.typography.headlineLarge,
                color = MathTextPrimary,
                letterSpacing = 2.sp
            )
            Text(
                text = "Desafía tu mente",
                style = MaterialTheme.typography.bodyMedium,
                color = MathTextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MathSurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo o Usuario", color = MathTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MathPurple,
                            unfocusedBorderColor = MathOutline,
                            focusedTextColor = MathTextPrimary,
                            unfocusedTextColor = MathTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = MathTextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MathPurple,
                            unfocusedBorderColor = MathOutline,
                            focusedTextColor = MathTextPrimary,
                            unfocusedTextColor = MathTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onLoginSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MathPurple)
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(onClick = onNavigateRegister) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MathCyan
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        onLoginSuccess = {},
        onNavigateRegister = {}
    )
}
