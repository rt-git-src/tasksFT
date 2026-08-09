package com.rustamft.tasksft.presentation.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.AppTextButton
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun ExportConfirmDialog(
    backupDirectory: String,
    onDismissClick: () -> Unit,
    onChooseDirectoryClick: () -> Unit,
    onExportTasksClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(text = stringResource(id = R.string.backup)) },
        text = {
            Text(
                text = stringResource(
                    id = R.string.backup_dialog_content,
                    backupDirectory,
                ),
            )
        },
        confirmButton = {
            AppTextButton(
                onClick = onChooseDirectoryClick,
                text = stringResource(id = R.string.backup_dialog_choose_dir),
            )
        },
        dismissButton = {
            AppTextButton(
                onClick = onExportTasksClick,
                text = stringResource(R.string.action_save),
            )
        },
        shape = AppCardShape,
        backgroundColor = AppTheme.glass.surfaceStrong,
        contentColor = AppTheme.glass.content,
    )
}

@Preview
@Composable
private fun ExportConfirmDialogPreview(
    @PreviewParameter(ExportConfirmDialogPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        ExportConfirmDialog(
            backupDirectory = "",
            onDismissClick = {},
            onChooseDirectoryClick = {},
            onExportTasksClick = {},
        )
    }
}

private class ExportConfirmDialogPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
