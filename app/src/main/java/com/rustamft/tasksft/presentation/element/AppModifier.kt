package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun Modifier.appTheme(
    shape: Shape = AppCardShape,
    containerColor: Color = AppTheme.glass.surface,
    elevation: Dp = 10.dp,
): Modifier {
    val glass = AppTheme.glass
    val topColor = containerColor.copy(
        alpha = (containerColor.alpha + 0.10f).coerceAtMost(1f),
    )
    return this
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
        )
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(topColor, containerColor),
            ),
        )
        .border(
            width = 1.dp,
            color = glass.border,
            shape = shape,
        )
}

@Composable
internal fun Modifier.appThemeControl(
    shape: Shape = AppControlShape,
): Modifier = appTheme(
    shape = shape,
    containerColor = AppTheme.glass.control,
    elevation = 0.dp,
)
