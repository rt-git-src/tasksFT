package com.rustamft.tasksft.presentation.screen.settings.model

import androidx.compose.runtime.Immutable
import com.rustamft.tasksft.domain.model.Preferences

@Immutable
internal data class SettingsUiState(
    val preferences: Preferences = Preferences(),
)
