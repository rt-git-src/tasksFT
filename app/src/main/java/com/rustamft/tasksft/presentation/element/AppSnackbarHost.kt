package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.glassSurface

@Composable
internal fun AppSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .glassSurface(
                    shape = AppCardShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                ),
            snackbarData = snackbarData,
            shape = AppCardShape,
            containerColor = Color.Transparent,
            contentColor = AppTheme.glass.content,
            actionContentColor = AppTheme.glass.accent,
            dismissActionContentColor = AppTheme.glass.accent,
        )
    }
}
