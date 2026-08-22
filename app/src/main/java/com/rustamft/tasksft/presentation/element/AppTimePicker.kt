package com.rustamft.tasksft.presentation.element

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rustamft.tasksft.presentation.global.format
import java.util.Calendar

@Composable
internal fun AppTimePicker(
    value: Long,
    onValueChange: (Long) -> Unit,
    themeResId: Int,
    modifier: Modifier = Modifier,
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = value }
    val dialog = TimePickerDialog(
        LocalContext.current,
        themeResId,
        { _, hour, minute ->
            val updated = Calendar.getInstance().apply {
                timeInMillis = value
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            onValueChange(updated.timeInMillis)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true,
    )
    AppValueControl(
        modifier = modifier,
        text = "${calendar.get(Calendar.HOUR_OF_DAY).format(2)}:${
            calendar.get(Calendar.MINUTE).format(2)
        }",
        onClick = dialog::show,
    )
}
