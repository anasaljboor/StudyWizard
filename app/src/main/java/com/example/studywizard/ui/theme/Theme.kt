package com.example.studywizard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SoftGreen,
    onPrimary = NavyBlue,
    background = NavyBlue,
    onBackground = White,
    surface = LightNavy,
    onSurface = White,
    secondary = SoftGreen,
    onSecondary = NavyBlue,
    error = ErrorRed,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = SoftGreen,
    onPrimary = NavyBlue,
    background = White,
    onBackground = DarkText,
    surface = LightGrey,
    onSurface = DarkText,
    secondary = NavyBlue,
    onSecondary = White,
    error = ErrorRed,
    onError = White
)

@Composable
fun StudyWizardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable for fixed branding
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
