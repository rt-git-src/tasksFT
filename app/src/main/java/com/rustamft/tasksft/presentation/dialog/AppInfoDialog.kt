package com.rustamft.tasksft.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.rustamft.tasksft.BuildConfig
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppInfoDialog(
    onDismiss: () -> Unit,
    onOpenGithub: () -> Unit,
) {
    AppDialog(
        title = stringResource(R.string.app_info),
        text = "${
            stringResource(R.string.app_info_dialog_content)
        } ${
            BuildConfig.VERSION_NAME
        }",
        primaryText = stringResource(R.string.action_close),
        secondaryText = "GitHub",
        onDismiss = onDismiss,
        onPrimaryClick = onDismiss,
        onSecondaryClick = onOpenGithub,
    )
}

@Preview
@Composable
private fun AppInfoDialogPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppInfoDialog(
            onDismiss = {},
            onOpenGithub = {},
        )
    }
}
