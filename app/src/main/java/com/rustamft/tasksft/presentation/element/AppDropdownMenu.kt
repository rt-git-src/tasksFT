package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar

@Composable
internal fun <T> AppDropdownMenu(
    modifier: Modifier = Modifier,
    itemToName: Map<T, UIText>,
    selectedItemState: MutableState<T>,
    onClickAdditional: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier.wrapContentSize()) {
        AppValueControl(
            modifier = Modifier,
            text = itemToName[selectedItemState.value]?.asString().orEmpty(),
            onClick = { expanded = true },
        )
        CompositionLocalProvider(LocalGlassHazeState provides null) {
            MaterialTheme(colors = AppTheme.colors.copy(surface = Color.Transparent)) {
                DropdownMenu(
                    modifier = Modifier.appTheme(
                        shape = AppControlShape,
                        tone = GlassTone.Strong,
                        elevation = 12.dp,
                    ),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    itemToName.forEach { (item, name) ->
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                selectedItemState.value = item
                                onClickAdditional()
                            },
                        ) {
                            Text(
                                text = name.asString(),
                                color = AppTheme.glass.content,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppDropdownMenuPreview() {
    AppTheme(theme = Preferences.Theme.Light) {
        AppDropdownMenu(
            itemToName = TaskViewState.CALENDAR_UNIT_TO_NAME,
            selectedItemState = remember { mutableIntStateOf(Calendar.DAY_OF_MONTH) },
            onClickAdditional = {},
        )
    }
}
