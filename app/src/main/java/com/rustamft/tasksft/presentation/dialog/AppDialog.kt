package com.rustamft.tasksft.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.AppTextButton
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.LocalGlassHazeState

@Composable
internal fun AppDialog(
    title: String,
    text: String,
    primaryText: String,
    secondaryText: String? = null,
    onDismiss: () -> Unit,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit = {},
    primaryEnabled: Boolean = true,
) {
    if (LocalInspectionMode.current) {
        AppAlertDialogContent(
            title = title,
            text = text,
            primaryText = primaryText,
            secondaryText = secondaryText,
            onPrimaryClick = onPrimaryClick,
            onSecondaryClick = onSecondaryClick,
            primaryEnabled = primaryEnabled,
        )
    } else {
        Dialog(onDismissRequest = onDismiss) {
            AppAlertDialogContent(
                title = title,
                text = text,
                primaryText = primaryText,
                secondaryText = secondaryText,
                onPrimaryClick = onPrimaryClick,
                onSecondaryClick = onSecondaryClick,
                primaryEnabled = primaryEnabled,
            )
        }
    }
}

@Composable
private fun AppAlertDialogContent(
    title: String,
    text: String,
    primaryText: String,
    secondaryText: String?,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    primaryEnabled: Boolean,
) {
    CompositionLocalProvider(LocalGlassHazeState provides null) {
        AppSurface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            tone = GlassTone.Strong,
            elevation = 16.dp,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    color = AppTheme.glass.content,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = text,
                    color = AppTheme.glass.content,
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    secondaryText?.let { text ->
                        AppTextButton(
                            text = text,
                            onClick = onSecondaryClick,
                        )
                    }
                    AppTextButton(
                        text = primaryText,
                        onClick = onPrimaryClick,
                        enabled = primaryEnabled,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppDialogPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppDialog(
            title = "Title",
            text = "Text",
            primaryText = "Primary",
            secondaryText = "Secondary",
            onDismiss = {},
            onPrimaryClick = {},
            onSecondaryClick = {},
        )
    }
}
