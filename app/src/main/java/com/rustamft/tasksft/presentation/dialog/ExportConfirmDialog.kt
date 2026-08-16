package com.rustamft.tasksft.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun ExportConfirmDialog(
    backupDirectory: String,
    onDismiss: () -> Unit,
    onChooseDirectory: () -> Unit,
    onExportTasks: () -> Unit,
) {
    AppDialog(
        title = stringResource(id = R.string.backup),
        text = stringResource(R.string.backup_dialog_content, backupDirectory),
        primaryText = stringResource(R.string.action_save),
        secondaryText = stringResource(id = R.string.backup_dialog_choose_dir),
        onDismiss = onDismiss,
        onPrimaryClick = onExportTasks,
        onSecondaryClick = onChooseDirectory,
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
            onDismiss = {},
            onChooseDirectory = {},
            onExportTasks = {},
        )
    }
}

private class ExportConfirmDialogPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
