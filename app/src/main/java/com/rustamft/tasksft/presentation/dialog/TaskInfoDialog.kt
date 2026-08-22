package com.rustamft.tasksft.presentation.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.global.toDateTime

@Composable
internal fun TaskInfoDialog(
    createTime: Long,
    onDismissClick: () -> Unit,
) {
    AppDialog(
        title = stringResource(R.string.task_info),
        text = stringResource(
            R.string.task_info_dialog_content,
            if (createTime == 0L) {
                stringResource(R.string.now)
            } else {
                val dateTime = createTime.toDateTime()
                "${dateTime.date} ${dateTime.time}"
            }
        ),
        primaryText = stringResource(R.string.action_close),
        onDismiss = onDismissClick,
        onPrimaryClick = onDismissClick,
    )
}
