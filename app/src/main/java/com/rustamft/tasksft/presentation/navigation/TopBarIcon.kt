package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.element.AppIconButton
import com.rustamft.tasksft.presentation.global.thenIf
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.glassControl


@Composable
internal fun TopBarIcon(item: NavItem) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .thenIf(condition = item.enabled) {
                glassControl(shape = CircleShape)
            },
        contentAlignment = Alignment.Center,
    ) {
        AppIconButton(
            painter = painterResource(item.painterResId),
            contentDescription = stringResource(item.descriptionResId),
            tint = AppTheme.glass.accent,
            onClick = item.onClick,
        )
    }
}
