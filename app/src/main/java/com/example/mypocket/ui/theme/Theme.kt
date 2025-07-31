package com.example.mypocket.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    secondary = RedSecondary,
    tertiary = RedTertiary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = OnLightPrimary,
    onSecondary = OnLightSecondary,
    onTertiary = Color.Black,
    onBackground = OnLightBackground,
    onSurface = OnLightSurface,
    surfaceVariant = LightSurfaceVariant,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = OnDarkPrimary,
    onSecondary = OnDarkSecondary,
    onTertiary = Color.Black,
    onBackground = OnDarkBackground,
    onSurface = OnDarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = Color.White
)


@Composable
fun MyPocketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ← changed from true to false
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