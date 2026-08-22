package com.rustamft.tasksft.presentation.screen.settings.model

internal sealed interface SettingsEffect {
    data object NavigateBack : SettingsEffect
}
