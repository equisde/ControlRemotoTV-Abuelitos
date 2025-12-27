package com.androiremote.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = HighContrastYellow,
    secondary = HighContrastWhite,
    tertiary = Pink80,
    background = HighContrastBlack,
    surface = HighContrastBlack,
    onPrimary = HighContrastBlack,
    onSecondary = HighContrastBlack,
    onTertiary = HighContrastBlack,
    onBackground = HighContrastWhite,
    onSurface = HighContrastWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = Pink40,
    background = HighContrastWhite,
    surface = HighContrastWhite,
    onPrimary = HighContrastWhite,
    onSecondary = HighContrastWhite,
    onTertiary = HighContrastWhite,
    onBackground = HighContrastBlack,
    onSurface = HighContrastBlack,
)

@Composable
fun AndroiRemoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
