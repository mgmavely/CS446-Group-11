package com.example.memento.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Pink60,
    secondary = Pink80,
    tertiary = Pink80,
    background = Pink60,
    surface = Pink60,
    onPrimary = White,
    onSecondary = White,
    onTertiary = Garnet,
    onBackground = Garnet,
    onSurface = Pink60,
)

private val LightColorScheme = lightColorScheme(
    primary = Pink40,
    secondary = Pink60,
    tertiary = Pink80,
    background = Pink40,
    surface = Pink40,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Garnet,
    onSurface = Pink60
)

@Composable
fun MementoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}