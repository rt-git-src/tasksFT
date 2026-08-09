package com.rustamft.tasksft.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rustamft.tasksft.domain.model.Preferences.Theme

private val DarkColorPalette = darkColors(
    primary = RedWisteria,
    primaryVariant = DeepBurgundy,
    secondary = MistBlue,
    secondaryVariant = White,
    background = NightBlue,
    onBackground = White,
    surface = DarkBlue,
    onSurface = IndigoWhite,
)

private val LightColorPalette = lightColors(
    primary = DeepBurgundy,
    primaryVariant = Sappanwood,
    secondary = IndigoWhite,
    secondaryVariant = White,
    onSecondary = Ink,
    background = WarmIvory,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
)

@Immutable
data class LiquidGlassColors(
    val surface: Color,
    val surfaceStrong: Color,
    val control: Color,
    val border: Color,
    val highlight: Color,
    val shadow: Color,
    val divider: Color,
    val content: Color,
    val contentMuted: Color,
    val accent: Color,
    val accentSoft: Color,
    val backgroundOverlay: Color,
    val dialogScrim: Color,
)

private val LightGlassPalette = LiquidGlassColors(
    surface = Color(0xB8FFF9F7),
    surfaceStrong = Color(0xE6FFF9F7),
    control = Color(0x8CFFFFFF),
    border = Color(0xB8FFFFFF),
    highlight = Color(0xE6FFFFFF),
    shadow = Color(0x3D5C2938),
    divider = Color(0x267E2639),
    content = Ink,
    contentMuted = Color(0xFF6F6265),
    accent = DeepBurgundy,
    accentSoft = Color(0x3D8F1F3A),
    backgroundOverlay = Color(0x38FFF8F5),
    dialogScrim = Color(0x520F172A),
)

private val DarkGlassPalette = LiquidGlassColors(
    surface = Color(0x8C172033),
    surfaceStrong = Color(0xD91B2436),
    control = Color(0x66323B4E),
    border = Color(0x4DFFFFFF),
    highlight = Color(0x66FFFFFF),
    shadow = Color(0x99030A16),
    divider = Color(0x38FFFFFF),
    content = Color(0xFFF8F4F5),
    contentMuted = Color(0xFFCBD0D9),
    accent = Color(0xFFE09AAF),
    accentSoft = Color(0x4DBB7796),
    backgroundOverlay = Color(0x78101A2A),
    dialogScrim = Color(0x99030A16),
)

private val LocalLiquidGlassColors = staticCompositionLocalOf { LightGlassPalette }
private val LocalIsDarkTheme = staticCompositionLocalOf { false }

object AppTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    val glass: LiquidGlassColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLiquidGlassColors.current

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkTheme.current

    val taskColors = listOf(
        PureCrimson,
        Corn,
        Patina,
        LapisLazuli,
        WisteriaPurple,
    )
}

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit,
) {
    val isDark = when (theme) {
        is Theme.Auto -> isSystemInDarkTheme()
        is Theme.Light -> false
        is Theme.Dark -> true
    }
    val colors = if (isDark) DarkColorPalette else LightColorPalette
    val glassColors = if (isDark) DarkGlassPalette else LightGlassPalette
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = !isDark,
    )
    CompositionLocalProvider(
        LocalLiquidGlassColors provides glassColors,
        LocalIsDarkTheme provides isDark,
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}
