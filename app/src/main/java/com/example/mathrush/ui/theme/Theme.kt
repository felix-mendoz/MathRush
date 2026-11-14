package com.example.mathrush.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MathRushColorScheme = darkColorScheme(
    primary = MathPurple,
    onPrimary = Color.White,
    primaryContainer = MathPurpleDark,
    onPrimaryContainer = MathTextPrimary,

    secondary = MathCyan,
    onSecondary = MathBackground,
    secondaryContainer = MathCyanDark,
    onSecondaryContainer = MathTextPrimary,

    tertiary = MathSuccess,
    onTertiary = MathBackground,
    tertiaryContainer = MathSuccessContainer,
    onTertiaryContainer = MathSuccess,

    background = MathBackground,
    onBackground = MathTextPrimary,

    surface = MathSurfaceCard,
    onSurface = MathTextPrimary,
    surfaceVariant = MathSurfaceVariant,
    onSurfaceVariant = MathTextSecondary,

    error = MathError,
    onError = MathBackground,
    errorContainer = MathErrorContainer,
    onErrorContainer = MathError,

    outline = MathOutline
)

@Composable
fun MathRushTheme(
    darkTheme: Boolean = true, // Forzamos el tema oscuro propio del juego
    dynamicColor: Boolean = false, // Desactivado para conservar la identidad visual de MathRush
    content: @Composable () -> Unit
) {
    val colorScheme = MathRushColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}