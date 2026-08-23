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
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.appPressable

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
                shape = CircleShape,
                role = Role.RadioButton,
                onClick = onClick,
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
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppColorButton(
            color = AppTheme.taskColors.first(),
            selected = true,
            contentDescription = "",
            onClick = {},
        )
    }
}
