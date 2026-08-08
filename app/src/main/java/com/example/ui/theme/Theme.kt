package com.example.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Emerald800,
    onPrimary = CardSurface,
    primaryContainer = Emerald100,
    onPrimaryContainer = Emerald900,
    secondary = Gold600,
    onSecondary = CardSurface,
    secondaryContainer = Gold100,
    onSecondaryContainer = Gold700,
    tertiary = Emerald600,
    background = WarmWhite,
    onBackground = Slate900,
    surface = CardSurface,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200
)

private val DarkColorScheme = darkColorScheme(
    primary = Emerald500,
    onPrimary = Emerald900,
    primaryContainer = Emerald800,
    onPrimaryContainer = Emerald100,
    secondary = Gold500,
    onSecondary = Slate900,
    secondaryContainer = Gold700,
    onSecondaryContainer = Gold100,
    tertiary = Emerald500,
    background = Slate900,
    onBackground = WarmWhite,
    surface = Slate900,
    onSurface = WarmWhite,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200,
    outline = Slate500
)

@Composable
fun ParwaazTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand colors primarily
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
