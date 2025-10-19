package com.example.tiendaonline.ui.theme

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
    primary = PetzonePrimary,
    secondary = PetzoneSecondary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = PetzoneWhite,
    onSecondary = PetzoneWhite,
    onBackground = PetzoneWhite,
    onSurface = PetzoneWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PetzonePrimary,
    secondary = PetzoneSecondary,
    background = PetzoneBackground,
    surface = PetzoneSurface,
    onPrimary = PetzoneWhite,
    onSecondary = PetzoneWhite,
    onBackground = PetzoneTextDark,
    onSurface = PetzoneTextDark
)

@Composable
fun TiendaOnlineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
        typography = PetzoneTypography,
        content = content
    )
}