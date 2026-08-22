package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.glass.accent,
        ),
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun AppTextButtonPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppTextButton(
            onClick = {},
            text = "Button",
        )
    }
}
