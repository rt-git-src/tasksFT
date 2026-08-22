package com.rustamft.tasksft.presentation.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetAllTasksUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.screen.list.model.ListUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ListViewModel(
    getAllTasksUseCase: GetAllTasksUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTasksUseCase: DeleteTaskUseCase,
    private val exceptionHandler: CoroutineExceptionHandler,
) : ViewModel() {

    val uiState: StateFlow<ListUiState> = getAllTasksUseCase
        .execute()
        .map(::ListUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListUiState(),
        )

    fun setTaskFinished(task: Task, finished: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            saveTaskUseCase.execute(task.copy(finished = finished))
        }
    }

    fun deleteFinishedTasks() {
        uiState.value.tasks
            .filter(Task::finished)
            .takeIf { it.isNotEmpty() }
            ?.let { tasks ->
                viewModelScope.launch(exceptionHandler) {
                    deleteTasksUseCase.execute(tasks)
                }
            }
    }
}
