package com.rustamft.tasksft.presentation.element

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Locale

@Composable
internal fun AppDatePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    themeResId: Int,
    modifier: Modifier = Modifier,
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = value }
    val dialog = DatePickerDialog(
        LocalContext.current,
        themeResId,
        { _, year, month, day ->
            val updated = Calendar.getInstance().apply {
                timeInMillis = value
                set(year, month, day)
            }
            onValueChange(updated.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    )
    AppValueControl(
        modifier = modifier,
        text = calendar.getDateText(),
        onClick = dialog::show,
    )
}

private fun Calendar.getDateText(): String = "${get(Calendar.DAY_OF_MONTH)} ${
    getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
} ${get(Calendar.YEAR)}"
