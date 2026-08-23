package com.rustamft.tasksft.presentation.element

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.global.format
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme
import java.util.Calendar

@Composable
internal fun AppTimePicker(
    modifier: Modifier = Modifier,
    value: Long,
    onValueChange: (Long) -> Unit,
) {
    val themeResId = when {
        AppTheme.isDark -> R.style.DateTimePickerDarkTheme
        else -> R.style.DateTimePickerLightTheme
    }
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

@Preview
@Composable
private fun AppTimePickerPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppTimePicker(
            value = 0,
            onValueChange = {},
        )
    }
}
