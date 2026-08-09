package com.rustamft.tasksft.presentation.element

import android.app.TimePickerDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.global.format
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.Shapes
import java.util.Calendar

@Composable
fun AppTimePicker(
    modifier: Modifier = Modifier,
    calendarState: State<Calendar>,
    themeResId: Int,
    onValueChange: () -> Unit,
) {
    val calendar by calendarState

    fun getStringFromCalendar(): String {
        return "${
            calendar.get(Calendar.HOUR_OF_DAY).format(2)
        }:${
            calendar.get(Calendar.MINUTE).format(2)
        }"
    }

    var text by remember { mutableStateOf(getStringFromCalendar()) }
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        themeResId,
        { _, hour: Int, minute: Int ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            text = getStringFromCalendar()
            onValueChange()
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true,
    )
    Button(
        modifier = modifier,
        onClick = {
            timePickerDialog.show()
        },
        content = {
            Text(text = text)
        },
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
}
