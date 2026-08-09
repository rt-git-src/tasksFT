package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.Shapes
import java.util.Calendar

@Composable
internal fun <T> AppDropdownMenu(
    modifier: Modifier = Modifier,
    itemToName: Map<T, UIText>,
    selectedItemState: MutableState<T>,
    onClickAdditional: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.wrapContentSize(),
    ) {
        Button(
            onClick = { expanded = true },
            content = { Text(text = itemToName[selectedItemState.value]?.asString() ?: "") },
            shape = Shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.glass.control,
                contentColor = AppTheme.glass.content,
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 1.dp,
            ),
        )
        DropdownMenu(
            modifier = Modifier.background(AppTheme.glass.surfaceStrong),
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
                    Text(name.asString())
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
