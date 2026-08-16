package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .appTheme(
                    shape = AppCardShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                ),
            snackbarData = snackbarData,
            shape = AppCardShape,
            backgroundColor = Color.Transparent,
            contentColor = AppTheme.glass.content,
            actionColor = AppTheme.glass.accent,
            elevation = 0.dp,
        )
    }
}
