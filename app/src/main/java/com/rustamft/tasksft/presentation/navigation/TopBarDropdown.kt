package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.LocalGlassHazeState
import com.rustamft.tasksft.presentation.theme.glassSurface

@Composable
internal fun TopBarDropdown(items: List<NavItem>) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box {
        TopBarIcon(
            item = NavItem(
                painterResId = R.drawable.ic_more_vert,
                descriptionResId = R.string.action_more,
                onClick = { expanded = true },
            ),
        )
        CompositionLocalProvider(LocalGlassHazeState provides null) {
            DropdownMenu(
                modifier = Modifier.glassSurface(
                    shape = AppControlShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                ),
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(item.descriptionResId),
                                color = AppTheme.glass.content,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(22.dp),
                                painter = painterResource(item.painterResId),
                                contentDescription = null,
                                tint = AppTheme.glass.accent,
                            )
                        },
                        enabled = item.enabled,
                        onClick = {
                            expanded = false
                            item.onClick()
                        },
                    )
                }
            }
        }
    }
}
