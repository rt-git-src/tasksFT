package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppColorButton(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .appPressable(
                onClick = onClick,
                role = Role.RadioButton,
            )
            .semantics {
                this.selected = selected
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .border(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) AppTheme.glass.accent else AppTheme.glass.rimTop,
                    shape = CircleShape,
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = color.copy(alpha = 1f), shape = CircleShape),
            )
        }
    }
}

@Preview
@Composable
private fun AppColorButtonPreview(
    @PreviewParameter(AppColorButtonPreviewParameter::class) themeAndColor: Pair<Color, Preferences.Theme>,
) {
    val (color, theme) = themeAndColor
    AppTheme(theme = theme) {
        AppColorButton(
            color = color,
            selected = true,
            contentDescription = "",
            onClick = {},
        )
    }
}

private class AppColorButtonPreviewParameter : PreviewParameterProvider<Pair<Color, Preferences.Theme>> {
    override val values = AppTheme.taskColors.associateWith { Preferences.Theme.Light }.toList()
        .plus(AppTheme.taskColors.associateWith { Preferences.Theme.Dark }.toList())
        .asSequence()
}
