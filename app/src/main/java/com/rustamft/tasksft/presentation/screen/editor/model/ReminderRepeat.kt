package com.rustamft.tasksft.presentation.screen.editor.model

import java.util.Calendar

internal enum class ReminderRepeat(val calendarUnit: Int) {
    NONE(0),
    DAILY(Calendar.DAY_OF_MONTH),
    WEEKLY(Calendar.WEEK_OF_MONTH),
    MONTHLY(Calendar.MONTH);

    companion object {
        fun fromCalendarUnit(calendarUnit: Int): ReminderRepeat {
            return entries.firstOrNull { it.calendarUnit == calendarUnit } ?: NONE
        }
    }
}
