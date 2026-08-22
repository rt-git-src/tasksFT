package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.glassSurface

@Composable
internal fun AppSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        val hasAction = snackbarData.visuals.actionLabel != null
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .glassSurface(
                        shape = AppCardShape,
                        tone = GlassTone.Strong,
                        elevation = 12.dp,
                    )
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(
                        start = 16.dp,
                        end = if (hasAction || snackbarData.visuals.withDismissAction) 4.dp else 16.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(vertical = 14.dp),
                text = snackbarData.visuals.message,
                color = AppTheme.glass.content,
                style = MaterialTheme.typography.bodyMedium,
            )
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                AppTextButton(
                    onClick = snackbarData::performAction,
                    text = actionLabel,
                )
            }
            if (snackbarData.visuals.withDismissAction) {
                AppTextButton(
                    onClick = snackbarData::dismiss,
                    text = stringResource(R.string.action_close),
                )
            }
        }
    }
}
