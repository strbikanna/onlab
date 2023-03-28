package com.example.languagelearningapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PurpleA100,
    primaryVariant = PurpleA700,
    secondary = Yellow200
)

private val LightColorPalette = lightColors(
    primary = PurpleA100,
    primaryVariant = PurpleA200,
    secondary = Yellow200,
    secondaryVariant = Yellow700,
    background = Color.White,
    surface = Color.White,
    onPrimary = PurpleA700,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,

)

@Composable
fun LanguageLearningAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}