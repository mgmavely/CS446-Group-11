package com.burgerbytes.memento.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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