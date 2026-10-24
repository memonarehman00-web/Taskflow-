package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = PrimaryDark,
    secondaryContainer = GoldLight,
    onSecondaryContainer = PrimaryDark,
    tertiary = EmeraldAccent,
    onTertiary = Color.White,
    background = BgCream,
    onBackground = TextMain,
    surface = SurfaceWhite,
    onSurface = TextMain,
    surfaceVariant = Color(0xFFF1F5F2),
    onSurfaceVariant = TextMuted,
    outline = BorderColor,
    error = DangerRed,
    onError = Color.White,
    errorContainer = DangerBg,
    onErrorContainer = DangerRed
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = PrimaryDark,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = PrimaryDark,
    tertiary = EmeraldAccent,
    onTertiary = Color.White,
    background = Color(0xFF0F1E17),
    onBackground = Color(0xFFE6EDE8),
    surface = Color(0xFF142920),
    onSurface = Color(0xFFE6EDE8),
    surfaceVariant = Color(0xFF1E3A2E),
    onSurfaceVariant = Color(0xFFA5B8AD),
    outline = Color(0xFF2E5343),
    error = Color(0xFFEF4444),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
