package com.rustamft.tasksft.presentation.screen.list.model

import androidx.compose.runtime.Immutable
import com.rustamft.tasksft.domain.model.Task

@Immutable
internal data class ListUiState(
    val tasks: List<Task> = emptyList(),
)
