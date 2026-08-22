package com.rustamft.tasksft.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.rustamft.tasksft.domain.model.Preferences.Theme

private val DarkColorPalette = darkColorScheme(
    primary = RedWisteria,
    onPrimary = NightBlue,
    primaryContainer = DeepBurgundy,
    onPrimaryContainer = White,
    secondary = MistBlue,
    onSecondary = NightBlue,
    secondaryContainer = DarkBlue,
    onSecondaryContainer = White,
    background = NightBlue,
    onBackground = White,
    surface = DarkBlue,
    onSurface = IndigoWhite,
    surfaceVariant = Color(0xFF323B4E),
    onSurfaceVariant = MistBlue,
    outline = Color(0xFF8D94A3),
)

private val LightColorPalette = lightColorScheme(
    primary = DeepBurgundy,
    onPrimary = White,
    primaryContainer = Sappanwood,
    onPrimaryContainer = White,
    secondary = IndigoWhite,
    onSecondary = Ink,
    secondaryContainer = Color(0xFFFFE8EE),
    onSecondaryContainer = Ink,
    background = WarmIvory,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF5E8EB),
    onSurfaceVariant = Color(0xFF6F6265),
    outline = Color(0xFF887176),
)

@Immutable
data class GlassColors(
    val surface: Color,
    val surfaceStrong: Color,
    val control: Color,
    val surfaceFallback: Color,
    val surfaceStrongFallback: Color,
    val controlFallback: Color,
    val rimTop: Color,
    val rimBottom: Color,
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

private val LightGlassPalette = GlassColors(
    surface = Color(0x52FFF9F7),
    surfaceStrong = Color(0x8AFFF9F7),
    control = Color(0x38FFFFFF),
    surfaceFallback = Color(0xD9FFF9F7),
    surfaceStrongFallback = Color(0xF2FFF9F7),
    controlFallback = Color(0xB8FFFFFF),
    rimTop = Color(0xF2FFFFFF),
    rimBottom = Color(0x4D9E6672),
    highlight = Color(0x70FFFFFF),
    shadow = Color(0x4D5C2938),
    divider = Color(0x267E2639),
    content = Ink,
    contentMuted = Color(0xFF6F6265),
    accent = DeepBurgundy,
    accentSoft = Color(0x3D8F1F3A),
    backgroundOverlay = Color(0x38FFF8F5),
    dialogScrim = Color(0x520F172A),
)

private val DarkGlassPalette = GlassColors(
    surface = Color(0x4D172033),
    surfaceStrong = Color(0x801B2436),
    control = Color(0x33323B4E),
    surfaceFallback = Color(0xD1192436),
    surfaceStrongFallback = Color(0xF01B2436),
    controlFallback = Color(0xB3323B4E),
    rimTop = Color(0x73FFFFFF),
    rimBottom = Color(0x52101626),
    highlight = Color(0x38FFFFFF),
    shadow = Color(0xB3030A16),
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
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val glass: GlassColors
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
    CompositionLocalProvider(
        LocalLiquidGlassColors provides glassColors,
        LocalIsDarkTheme provides isDark,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}
