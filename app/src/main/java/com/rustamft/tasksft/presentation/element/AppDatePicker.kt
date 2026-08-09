package com.rustamft.tasksft.presentation.element

import android.app.DatePickerDialog
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
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.Shapes
import java.util.Calendar
import java.util.Locale

@Composable
internal fun AppDatePicker(
    modifier: Modifier = Modifier,
    calendarState: State<Calendar>,
    themeResId: Int,
    onValueChange: () -> Unit,
) {
    val calendar by calendarState
    var text by remember { mutableStateOf(calendar.getDateText()) }
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        themeResId,
        { _, year: Int, month: Int, day: Int ->
            calendar.set(year, month, day)
            text = calendar.getDateText()
            onValueChange()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    )
    Button(
        modifier = modifier,
        onClick = {
            datePickerDialog.show()
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

private fun Calendar.getDateText(): String {
    return "${
        get(Calendar.DAY_OF_MONTH)
    } ${
        getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault(),
        )
    } ${
        get(Calendar.YEAR)
    }"
}
