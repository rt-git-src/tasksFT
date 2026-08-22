package com.rustamft.tasksft.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.usecase.ExportTasksUseCase
import com.rustamft.tasksft.domain.usecase.GetPreferencesUseCase
import com.rustamft.tasksft.domain.usecase.ImportTasksUseCase
import com.rustamft.tasksft.domain.usecase.SavePreferencesUseCase
import com.rustamft.tasksft.presentation.global.SnackbarFlow
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.screen.settings.model.SettingsEffect
import com.rustamft.tasksft.presentation.screen.settings.model.SettingsUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    getPreferencesUseCase: GetPreferencesUseCase,
    private val savePreferencesUseCase: SavePreferencesUseCase,
    private val exportTasksUseCase: ExportTasksUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val snackbarFlow: SnackbarFlow,
    private val exceptionHandler: CoroutineExceptionHandler,
) : ViewModel() {

    private val preferencesFlow = getPreferencesUseCase.execute()
    val uiState: StateFlow<SettingsUiState> = preferencesFlow
        .map(::SettingsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState(),
        )

    private val effectChannel = Channel<SettingsEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun setTheme(theme: Preferences.Theme) {
        viewModelScope.launch(exceptionHandler) {
            savePreferencesUseCase.execute(preferencesFlow.first().copy(theme = theme))
        }
    }

    fun exportTasks(directoryUri: String) {
        viewModelScope.launch(exceptionHandler) {
            exportTasksUseCase.execute(directoryUriString = directoryUri)
            savePreferencesUseCase.execute(
                preferencesFlow.first().copy(backupDirectory = directoryUri),
            )
            completeTransfer(UIText.StringResource(R.string.backup_file_exported))
        }
    }

    fun importTasks(fileUri: String) {
        viewModelScope.launch(exceptionHandler) {
            importTasksUseCase.execute(fileUriString = fileUri)
            completeTransfer(UIText.StringResource(R.string.backup_file_imported))
        }
    }

    private suspend fun completeTransfer(message: UIText) {
        snackbarFlow.emit(message)
        effectChannel.send(SettingsEffect.NavigateBack)
    }
}
