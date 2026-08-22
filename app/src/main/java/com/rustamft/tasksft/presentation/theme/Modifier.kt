package com.rustamft.tasksft.presentation.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

internal enum class GlassTone {
    Regular,
    Strong,
    Control,
}

internal val LocalGlassHazeState = staticCompositionLocalOf<HazeState?> { null }

@Composable
internal fun Modifier.glassSurface(
    shape: Shape = AppCardShape,
    tone: GlassTone = GlassTone.Regular,
    elevation: Dp = 8.dp,
): Modifier {
    val glass = AppTheme.glass
    val hazeState = LocalGlassHazeState.current
    val tint: Color
    val fallbackTint: Color
    val blurRadius: Dp
    when (tone) {
        GlassTone.Regular -> {
            tint = glass.surface
            fallbackTint = glass.surfaceFallback
            blurRadius = 20.dp
        }

        GlassTone.Strong -> {
            tint = glass.surfaceStrong
            fallbackTint = glass.surfaceStrongFallback
            blurRadius = 24.dp
        }

        GlassTone.Control -> {
            tint = glass.control
            fallbackTint = glass.controlFallback
            blurRadius = 16.dp
        }
    }
    val rimBrush = Brush.verticalGradient(
        0f to glass.rimTop,
        0.55f to glass.rimTop.copy(alpha = glass.rimTop.alpha * 0.35f),
        1f to glass.rimBottom,
    )
    var result = this
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = glass.shadow,
            spotColor = glass.shadow,
        )
        .clip(shape)
    result = if (hazeState != null) {
        result.hazeEffect(
            state = hazeState,
            style = HazeStyle(
                backgroundColor = AppTheme.colors.background,
                tint = HazeTint(tint),
                blurRadius = blurRadius,
                noiseFactor = 0.04f,
                fallbackTint = HazeTint(fallbackTint),
            ),
        )
    } else {
        result.background(fallbackTint)
    }
    return result.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        val rimPath = Path().apply {
            when (outline) {
                is Outline.Rectangle -> addRect(outline.rect)
                is Outline.Rounded -> addRoundRect(outline.roundRect)
                is Outline.Generic -> addPath(outline.path)
            }
        }
        val rimWidth = 1.dp.toPx()
        val glintBrush = Brush.linearGradient(
            colors = listOf(glass.highlight, Color.Transparent),
            start = Offset.Zero,
            end = Offset(size.width * 0.72f, size.height * 0.62f),
        )
        val sheenBrush = Brush.radialGradient(
            colors = listOf(
                glass.highlight.copy(alpha = glass.highlight.alpha * 0.18f),
                Color.Transparent,
            ),
            center = Offset(size.width * 0.18f, 0f),
            radius = maxOf(size.width, size.height) * 0.9f,
        )
        onDrawWithContent {
            drawPath(path = rimPath, brush = sheenBrush)
            drawContent()
            drawPath(
                path = rimPath,
                brush = rimBrush,
                style = Stroke(width = rimWidth),
            )
            drawPath(
                path = rimPath,
                brush = glintBrush,
                style = Stroke(width = rimWidth * 0.55f),
            )
        }
    }
}

@Composable
internal fun Modifier.glassControl(
    shape: Shape = AppControlShape,
): Modifier = glassSurface(
    shape = shape,
    tone = GlassTone.Control,
    elevation = 0.dp,
)

@Composable
internal fun Modifier.appPressable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    role: Role? = null,
    pressedScale: Float = 0.97f,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = rememberPressedScale(interactionSource, pressedScale)
    val indication = LocalIndication.current
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            role = role,
            onClick = onClick,
        )
}

@Composable
internal fun Modifier.appToggleable(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    role: Role,
    pressedScale: Float = 0.97f,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = rememberPressedScale(interactionSource, pressedScale)
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .toggleable(
            value = value,
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            role = role,
            onValueChange = onValueChange,
        )
}

@Composable
private fun rememberPressedScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float,
): Float {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = tween(durationMillis = 110),
        label = "glassPressedScale",
    )
    return scale
}
