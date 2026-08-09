package com.rustamft.tasksft.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_EXPORT
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_RESTORE
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_CONTROL
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SettingsScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider,
) : ComposeScreen<SettingsScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag(TAG_SETTINGS_SCREEN) },
) {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    val backTopBarAction = child<KNode> {
        hasContentDescription(this@SettingsScreen.context.getString(R.string.action_back))
    }
    val themeControl = child<KNode> {
        hasTestTag(TAG_SETTINGS_THEME_CONTROL)
    }
    val exportAction = child<KNode> {
        hasTestTag(TAG_SETTINGS_EXPORT)
    }
    val restoreAction = child<KNode> {
        hasTestTag(TAG_SETTINGS_RESTORE)
    }
}
