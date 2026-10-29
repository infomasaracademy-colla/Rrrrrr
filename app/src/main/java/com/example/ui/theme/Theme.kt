package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RiskBrightRed,
    onPrimary = Color.White,
    primaryContainer = RiskDarkRed,
    onPrimaryContainer = Color.White,
    secondary = RiskGold,
    onSecondary = Color.Black,
    secondaryContainer = RiskAmber,
    onSecondaryContainer = Color.White,
    tertiary = RiskAmber,
    background = RiskBlack,
    onBackground = RiskTextPrimary,
    surface = RiskDarkSurface,
    onSurface = RiskTextPrimary,
    surfaceVariant = RiskCardDark,
    onSurfaceVariant = RiskTextSecondary,
    outline = RiskCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = RiskPrimaryRed,
    onPrimary = Color.White,
    primaryContainer = RiskLightRedBg,
    onPrimaryContainer = RiskDarkRed,
    secondary = RiskAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF4A3800),
    tertiary = RiskGold,
    background = RiskLightBg,
    onBackground = RiskLightTextPrimary,
    surface = RiskLightSurface,
    onSurface = RiskLightTextPrimary,
    surfaceVariant = Color(0xFFF1F3F5),
    onSurfaceVariant = RiskLightTextSecondary,
    outline = RiskLightCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek brand dark theme matching the restaurant logo
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
