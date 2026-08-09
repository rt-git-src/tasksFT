package com.rustamft.tasksft.presentation.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.BuildConfig
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.AppTextButton
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppInfoDialog(
    onDismissClick: () -> Unit,
    onOpenGithubClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(text = stringResource(id = R.string.app_info)) },
        text = {
            Text(
                text = "${
                    stringResource(id = R.string.app_info_dialog_content)
                } ${
                    BuildConfig.VERSION_NAME
                }",
            )
        },
        confirmButton = {
            AppTextButton(
                onClick = onDismissClick,
                text = stringResource(R.string.action_close),
            )
        },
        dismissButton = {
            AppTextButton(
                onClick = onOpenGithubClick,
                text = "GitHub",
            )
        },
        shape = AppCardShape,
        backgroundColor = AppTheme.glass.surfaceStrong,
        contentColor = AppTheme.glass.content,
    )
}


@Preview
@Composable
private fun AppInfoDialogPreview(
    @PreviewParameter(AppInfoDialogPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppInfoDialog(
            onDismissClick = {},
            onOpenGithubClick = {},
        )
    }
}

private class AppInfoDialogPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
