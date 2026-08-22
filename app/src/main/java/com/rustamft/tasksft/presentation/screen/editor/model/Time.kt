package com.rustamft.tasksft.presentation.screen.editor.model

import java.util.Calendar

internal fun defaultReminderEpochMillis(): Long {
    return Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.MINUTE, 0)
        add(Calendar.HOUR_OF_DAY, 1)
    }.timeInMillis
}
