package com.rustamft.tasksft.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.domain.model.Preferences

internal class ThemePreviewProvider : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
