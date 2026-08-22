package com.rustamft.tasksft.presentation.screen.editor.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class EditorUiState(
    val id: Int?,
    val created: Long,
    val title: String,
    val description: String,
    val reminderEnabled: Boolean,
    val reminderEpochMillis: Long,
    val reminderRepeat: ReminderRepeat,
    val finished: Boolean,
    val colorIndex: Int,
    val saveEnabled: Boolean,
)
