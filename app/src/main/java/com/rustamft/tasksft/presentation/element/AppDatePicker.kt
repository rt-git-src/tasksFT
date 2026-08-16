package com.rustamft.tasksft.presentation.element

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    var text by remember(calendar.timeInMillis) { mutableStateOf(calendar.getDateText()) }
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
    AppValueControl(
        modifier = modifier,
        text = text,
        onClick = datePickerDialog::show,
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
