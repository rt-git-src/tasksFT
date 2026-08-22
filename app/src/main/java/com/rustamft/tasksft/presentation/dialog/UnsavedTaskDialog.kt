package com.rustamft.tasksft.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun UnsavedTaskDialog(
    saveEnabled: Boolean,
    onDismiss: () -> Unit,
    onSaveTask: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    AppDialog(
        title = stringResource(R.string.task_unsaved),
        text = stringResource(R.string.task_unsaved_dialog_content),
        primaryText = stringResource(R.string.action_save),
        secondaryText = stringResource(R.string.action_discard),
        onDismiss = onDismiss,
        onPrimaryClick = {
            onSaveTask()
            onDismiss()
        },
        onSecondaryClick = {
            onNavigateBack()
            onDismiss()
        },
        primaryEnabled = saveEnabled,
    )
}


@Preview
@Composable
private fun TopBarPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        UnsavedTaskDialog(
            saveEnabled = true,
            onDismiss = {},
            onSaveTask = {},
            onNavigateBack = {},
        )
    }
}
