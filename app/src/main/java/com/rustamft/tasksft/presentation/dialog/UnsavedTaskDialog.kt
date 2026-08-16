package com.rustamft.tasksft.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.R

@Composable
internal fun UnsavedTaskDialog(
    onDismiss: () -> Unit,
    onSaveTask: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    AppDialog(
        title = stringResource(id = R.string.task_unsaved),
        text = stringResource(id = R.string.task_unsaved_dialog_content),
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
    )
}
