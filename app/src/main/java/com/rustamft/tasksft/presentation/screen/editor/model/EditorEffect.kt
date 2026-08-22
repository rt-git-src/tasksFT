package com.rustamft.tasksft.presentation.screen.editor.model

internal sealed interface EditorEffect {
    data object NavigateBack : EditorEffect
}
