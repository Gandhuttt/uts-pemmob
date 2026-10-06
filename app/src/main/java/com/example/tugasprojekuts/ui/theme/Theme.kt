package com.example.tugasprojekuts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Mint,
    onPrimary = DarkPaper,
    background = DarkPaper,
    surface = DarkPaper,
    onBackground = DarkInk,
    onSurface = DarkInk
)

private val LightColorScheme = lightColorScheme(
    primary = Ink,
    onPrimary = Paper,
    background = Paper,
    surface = Paper,
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun TugasProjekUtsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}