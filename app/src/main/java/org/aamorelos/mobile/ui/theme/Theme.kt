package org.aamorelos.mobile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8FC1F0),
    onPrimary = Color(0xFF0F2F55),
    primaryContainer = NavyPrimary,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFB0C4DE),
    secondaryContainer = Color(0xFF334769),
    onSecondaryContainer = Color(0xFFD6EAF8),
    tertiary = Color(0xFFB0C4DE),
    background = Color(0xFF0F1317),
    onBackground = Color(0xFFE8EDF3),
    surface = Color(0xFF141A21),
    onSurface = Color(0xFFE8EDF3),
    surfaceVariant = Color(0xFF1F2833),
    onSurfaceVariant = Color(0xFFB9C4D0),
    outline = Color(0xFF4A5A6E),
    outlineVariant = Color(0xFF3A444E)
)

private val LightColorScheme = lightColorScheme(
    primary = NavyMedium,
    onPrimary = Color.White,
    primaryContainer = NavyPrimary,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF5B6B82),
    secondaryContainer = BlueLight,
    onSecondaryContainer = NavyDark,
    tertiary = Color(0xFF5B6B82),
    background = Color(0xFFF7F9FB),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE8EDF3),
    onSurfaceVariant = Color(0xFF5B6B82),
    outline = Color(0xFF8FA0B3),
    outlineVariant = Color(0xFFC5D0DD)
)

@Composable
fun AAMorelosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Color dinámico desactivado por defecto para mantener el azul institucional en ambos modos
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}