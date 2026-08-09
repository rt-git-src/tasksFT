package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    text: String,
) {
    TextButton(
        onClick = onClick,
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
    @PreviewParameter(AppTextButtonPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppTextButton(
            onClick = {},
            text = "Button",
        )
    }
}

private class AppTextButtonPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
