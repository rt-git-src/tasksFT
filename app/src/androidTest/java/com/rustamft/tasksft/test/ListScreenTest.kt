package com.rustamft.tasksft.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.components.composesupport.interceptors.behavior.impl.systemsafety.SystemDialogSafetySemanticsBehaviorInterceptor
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.rustamft.tasksft.presentation.activity.MainActivity
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CHECKBOX
import com.rustamft.tasksft.screen.EditorScreen
import com.rustamft.tasksft.screen.ListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ListScreenTest :
    TestCase(
        kaspressoBuilder =
            Kaspresso.Builder.withComposeSupport(
                customize = {
                    flakySafetyParams = FlakySafetyParams.custom(timeoutMs = 5000, intervalMs = 1000)
                },
                lateComposeCustomize = { composeBuilder ->
                    composeBuilder.semanticsBehaviorInterceptors =
                        composeBuilder.semanticsBehaviorInterceptors
                            .filter {
                                it !is SystemDialogSafetySemanticsBehaviorInterceptor
                            }.toMutableList()
                },
            ),
    ) {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun taskIsCreatedAndRemovedCorrectly() =
        run {
            val taskTitle = "Task ${Random.nextInt(999999999)}"

            step("Click add FAB") {
                onComposeScreen<ListScreen>(composeTestRule) {
                    addFab { performClick() }
                }
            }
            step("Create new task") {
                onComposeScreen<EditorScreen>(composeTestRule) {
                    saveFab { assertDoesNotExist() }
                    titleEditText { performTextInput(taskTitle) }
                    secondColor {
                        assertIsDisplayed()
                        performClick()
                    }
                    saveFab {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
            step("Finish created task and open it") {
                onComposeScreen<ListScreen>(composeTestRule) {
                    taskCard {
                        assertIsDisplayed()
                        val checkbox =
                            child<KNode> {
                                hasTestTag(TAG_LIST_SCREEN_TASK_CHECKBOX)
                            }
                        checkbox {
                            assertIsDisplayed()
                            assertIsToggleable()
                            performClick()
                        }
                        val text = child<KNode> { hasText(taskTitle) }
                        text {
                            assertExists()
                            performClick()
                        }
                    }
                }
            }
            step("Check the task saved correctly and delete it") {
                onComposeScreen<EditorScreen>(composeTestRule) {
                    titleEditText {
                        assertTextEquals(taskTitle)
                    }
                    deleteTopBarAction {
                        performClick()
                    }
                }
            }
            step("Check the task doesn't exist anymore") {
                onComposeScreen<ListScreen>(composeTestRule) {
                    val text = child<KNode> { hasText(taskTitle) }
                    text {
                        assertDoesNotExist()
                    }
                }
            }
        }

    @Test
    fun settingsUseDirectThemeSelectionAndKeepBackupInteractions() =
        run {
            step("Open settings") {
                onComposeScreen<ListScreen>(composeTestRule) {
                    settingsTopBarAction {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
            step("Select the dark theme directly") {
                onComposeScreen<com.rustamft.tasksft.screen.SettingsScreen>(composeTestRule) {
                    darkTheme {
                        assertIsDisplayed()
                        assertIsSelectable()
                        performClick()
                    }
                    exportAction { assertIsDisplayed() }
                    restoreAction { assertIsDisplayed() }
                }
            }
            step("Return to the task list") {
                onComposeScreen<com.rustamft.tasksft.screen.SettingsScreen>(composeTestRule) {
                    backTopBarAction { performClick() }
                }
                onComposeScreen<ListScreen>(composeTestRule) {
                    addFab { assertIsDisplayed() }
                }
            }
            step("The selected theme persists") {
                onComposeScreen<ListScreen>(composeTestRule) {
                    settingsTopBarAction { performClick() }
                }
                onComposeScreen<com.rustamft.tasksft.screen.SettingsScreen>(composeTestRule) {
                    darkTheme { assertIsSelected() }
                    backTopBarAction { performClick() }
                }
            }
        }
}
