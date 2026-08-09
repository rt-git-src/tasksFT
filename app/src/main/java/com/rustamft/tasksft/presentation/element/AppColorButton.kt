package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppColorButton(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size = if (selected) 50.dp else 42.dp)
            .clip(shape = CircleShape)
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) AppTheme.glass.accent else AppTheme.glass.border,
                shape = CircleShape,
            )
            .background(color = color.copy(alpha = 1f))
            .clickable(onClick = onClick),
    )
}

@Preview
@Composable
private fun AppColorButtonPreview() {
    AppTheme(theme = Theme.Dark) {
        AppColorButton(
            color = AppTheme.taskColors[0],
            selected = true,
            onClick = {},
        )
    }
}
