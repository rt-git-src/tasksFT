package com.rustamft.tasksft.presentation.screen.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.usecase.DeleteTaskUseCase
import com.rustamft.tasksft.domain.usecase.GetTaskUseCase
import com.rustamft.tasksft.domain.usecase.SaveTaskUseCase
import com.rustamft.tasksft.presentation.global.SnackbarFlow
import com.rustamft.tasksft.presentation.global.toTimeDifference
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.screen.editor.model.EditorEffect
import com.rustamft.tasksft.presentation.screen.editor.model.EditorUiState
import com.rustamft.tasksft.presentation.screen.editor.model.ReminderRepeat
import com.rustamft.tasksft.presentation.screen.editor.model.defaultReminderEpochMillis
import com.rustamft.tasksft.presentation.theme.TaskColorIndices
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EditorViewModel(
    private val taskId: Int?,
    getTaskUseCase: GetTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val snackbarFlow: SnackbarFlow,
    private val exceptionHandler: CoroutineExceptionHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EditorUiState(
            id = null,
            created = 0L,
            title = "",
            description = "",
            reminderEnabled = false,
            reminderEpochMillis = defaultReminderEpochMillis(),
            reminderRepeat = ReminderRepeat.NONE,
            finished = false,
            colorIndex = TaskColorIndices.random(),
            saveEnabled = false,
        )
    )
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val effectChannel = Channel<EditorEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    init {
        if (taskId != null) {
            viewModelScope.launch(exceptionHandler) {
                getTaskUseCase.execute(taskId).first()?.let(::loadTask)
            }
        }
    }

    fun onTitleChange(title: String) {
        updateState { copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        updateState { copy(description = description) }
    }

    fun onColorChange(colorIndex: Int) {
        updateState { copy(colorIndex = colorIndex) }
    }

    fun onReminderEnabledChange(enabled: Boolean) {
        updateState { copy(reminderEnabled = enabled) }
    }

    fun onReminderChange(reminderEpochMillis: Long) {
        updateState { copy(reminderEpochMillis = reminderEpochMillis) }
    }

    fun onReminderRepeatChange(reminderRepeat: ReminderRepeat) {
        updateState { copy(reminderRepeat = reminderRepeat) }
    }

    fun saveTask() {
        val state = uiState.value
        if (!state.saveEnabled) return
        viewModelScope.launch(exceptionHandler) {
            saveTaskUseCase.execute(state.toTask())
            if (state.reminderEnabled) {
                val difference = state.reminderEpochMillis.toTimeDifference()
                snackbarFlow.emit(
                    UIText.StringResource(
                        R.string.reminder_in,
                        difference.days,
                        difference.hours,
                        difference.minutes,
                    ),
                )
            }
            effectChannel.send(EditorEffect.NavigateBack)
        }
    }

    fun deleteTask() {
        val state = uiState.value
        viewModelScope.launch(exceptionHandler) {
            deleteTaskUseCase.execute(state.toTask())
            effectChannel.send(EditorEffect.NavigateBack)
        }
    }

    private fun loadTask(task: Task) {
        _uiState.value = EditorUiState(
            id = task.id,
            created = task.created,
            title = task.title,
            description = task.description,
            reminderEnabled = task.reminder > 0L,
            reminderEpochMillis = task.reminder.takeIf { it > 0L } ?: defaultReminderEpochMillis(),
            reminderRepeat = ReminderRepeat.fromCalendarUnit(task.repeatCalendarUnit),
            finished = task.finished,
            colorIndex = task.colorIndex,
            saveEnabled = false,
        )
    }

    private fun updateState(transform: EditorUiState.() -> EditorUiState) {
        _uiState.update { state ->
            val updated = state.transform()
            updated.copy(saveEnabled = updated.title.isNotBlank())
        }
    }

    private fun EditorUiState.toTask(): Task {
        val now = System.currentTimeMillis()
        return Task(
            id = id ?: (now % Int.MAX_VALUE).toInt(),
            created = created.takeIf { it != 0L } ?: now,
            title = title,
            description = description,
            reminder = reminderEpochMillis.takeIf { reminderEnabled } ?: 0L,
            repeatCalendarUnit = reminderRepeat.calendarUnit.takeIf { reminderEnabled } ?: 0,
            finished = finished,
            colorIndex = colorIndex,
        )
    }
}
