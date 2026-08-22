package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.LocalGlassHazeState
import com.rustamft.tasksft.presentation.theme.glassSurface

@Composable
internal fun <T> AppDropdownMenu(
    itemToName: Map<T, UIText>,
    value: T,
    onValueChange: (T) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.wrapContentSize()) {
        AppValueControl(
            text = itemToName[value]?.asString().orEmpty(),
            onClick = { onExpandedChange(true) },
        )
        CompositionLocalProvider(LocalGlassHazeState provides null) {
            DropdownMenu(
                modifier = Modifier.glassSurface(
                    shape = AppControlShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                ),
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                containerColor = Color.Transparent,
                shadowElevation = 0.dp,
            ) {
                itemToName.forEach { (item, name) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = name.asString(),
                                color = AppTheme.glass.content,
                            )
                        },
                        onClick = {
                            onExpandedChange(false)
                            onValueChange(item)
                        },
                    )
                }
            }
        }
    }
}
