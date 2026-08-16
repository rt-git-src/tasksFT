package com.rustamft.tasksft.presentation.screen.editor

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.annotation.parameters.DeepLink
import com.ramcosta.composedestinations.annotation.parameters.FULL_ROUTE_PLACEHOLDER
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.dialog.TaskInfoDialog
import com.rustamft.tasksft.presentation.dialog.UnsavedTaskDialog
import com.rustamft.tasksft.presentation.element.AppBackground
import com.rustamft.tasksft.presentation.element.AppColorButton
import com.rustamft.tasksft.presentation.element.AppDatePicker
import com.rustamft.tasksft.presentation.element.AppDropdownMenu
import com.rustamft.tasksft.presentation.element.AppIconButton
import com.rustamft.tasksft.presentation.element.AppSnackbarHost
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.AppTimePicker
import com.rustamft.tasksft.presentation.global.DEEP_LINK_URI
import com.rustamft.tasksft.presentation.global.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_COLOR
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_EDITTEXT_TITLE
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_FAB
import com.rustamft.tasksft.presentation.global.TASK_ID
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination<RootGraph>(
    route = ROUTE_EDITOR,
    deepLinks = [DeepLink(uriPattern = "$DEEP_LINK_URI$FULL_ROUTE_PLACEHOLDER")],
)
@Composable
internal fun EditorScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    taskId: Int?,
    viewModel: EditorViewModel = koinViewModel(
        parameters = {
            parametersOf(
                Bundle().apply {
                    taskId?.let { id -> putInt(TASK_ID, id) }
                },
            )
        },
    ),
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }
    EditorScreenContent(
        scaffoldState = scaffoldState,
        taskViewState = remember { mutableStateOf(viewModel.taskViewState) },
        openTaskInfoDialogState = viewModel.openTaskInfoDialogState,
        openUnsavedTaskDialogState = viewModel.openUnsavedTaskDialogState,
        valueChangedState = viewModel.valueChangedState,
        onNavigateBack = { navigator.popBackStack() },
        onSaveTask = { viewModel.saveTask() },
        onDeleteTask = { viewModel.deleteTask() },
    )
}

@Composable
private fun EditorScreenContent(
    scaffoldState: ScaffoldState,
    taskViewState: State<TaskViewState>,
    openTaskInfoDialogState: MutableState<Boolean>,
    openUnsavedTaskDialogState: MutableState<Boolean>,
    valueChangedState: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onSaveTask: () -> Unit,
    onDeleteTask: () -> Unit,
) {
    val state by taskViewState
    val onValueChange = {
        if (state.title.isBlank()) {
            valueChangedState.value = false
        } else if (!valueChangedState.value) {
            valueChangedState.value = true
        }
    }
    val pickerDialogThemeResId = if (AppTheme.isDark) {
        R.style.DateTimePickerDarkTheme
    } else {
        R.style.DateTimePickerLightTheme
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_EDITOR_SCREEN),
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackbarHost(hostState = it) },
        backgroundColor = Color.Transparent,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.screen_edit_task),
                backButton = {
                    AppIconButton(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        tint = AppTheme.glass.content,
                        onClick = {
                            if (valueChangedState.value) {
                                openUnsavedTaskDialogState.value = true
                            } else {
                                onNavigateBack()
                            }
                        },
                    )
                },
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_delete,
                        descriptionResId = R.string.action_delete,
                        onClick = onDeleteTask,
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.task_info,
                        onClick = { openTaskInfoDialogState.value = true },
                    ),
                ),
            )
        },
        floatingActionButton = {
            Fab(
                modifier = Modifier.testTag(TAG_EDITOR_SCREEN_FAB),
                item = NavItem(
                    painterResId = R.drawable.ic_save,
                    descriptionResId = R.string.action_save,
                    onClick = onSaveTask,
                ),
                visibilityState = valueChangedState,
            )
        },
    ) { paddingValues ->
        val fieldColors = TextFieldDefaults.textFieldColors(
            textColor = AppTheme.glass.content,
            cursorColor = AppTheme.glass.accent,
            placeholderColor = AppTheme.glass.contentMuted,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = AppTheme.glass.accent,
            unfocusedIndicatorColor = AppTheme.glass.divider,
            disabledIndicatorColor = AppTheme.glass.divider,
        )
        val switchColors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = AppTheme.glass.accent,
            uncheckedThumbColor = AppTheme.glass.contentMuted,
            uncheckedTrackColor = AppTheme.glass.control,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = paddingValues.calculateBottomPadding() + 124.dp),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TAG_EDITOR_SCREEN_EDITTEXT_TITLE),
                value = state.title,
                onValueChange = {
                    state.title = it
                    onValueChange()
                },
                label = { Text(text = stringResource(id = R.string.task_title)) },
                singleLine = true,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = fieldColors,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = {
                    state.description = it
                    onValueChange()
                },
                label = { Text(text = stringResource(id = R.string.task_description)) },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = fieldColors,
            )

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(id = R.string.task_color),
                color = AppTheme.glass.contentMuted,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppTheme.taskColors.forEachIndexed { index, color ->
                    AppColorButton(
                        modifier = Modifier.testTag("${TAG_EDITOR_SCREEN_COLOR}_$index"),
                        color = color,
                        selected = state.color == color,
                        contentDescription = stringResource(
                            id = R.string.task_color_option,
                            index + 1,
                        ),
                        onClick = {
                            if (state.color != color) {
                                state.color = color
                                onValueChange()
                            }
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            AppSurface(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_reminder),
                            contentDescription = null,
                            tint = AppTheme.glass.accent,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(id = R.string.reminder),
                            color = AppTheme.glass.content,
                            style = MaterialTheme.typography.subtitle1,
                        )
                        Switch(
                            checked = state.isReminderSet,
                            onCheckedChange = {
                                state.isReminderSet = it
                                onValueChange()
                            },
                            colors = switchColors,
                        )
                    }
                    if (state.isReminderSet) {
                        ReminderDivider()
                        ReminderControlRow(
                            iconResId = R.drawable.ic_event,
                            label = stringResource(id = R.string.reminder_date),
                        ) {
                            AppDatePicker(
                                calendarState = remember { mutableStateOf(state.reminder) },
                                themeResId = pickerDialogThemeResId,
                                onValueChange = onValueChange,
                            )
                        }
                        ReminderDivider()
                        ReminderControlRow(
                            iconResId = R.drawable.ic_time,
                            label = stringResource(id = R.string.reminder_time),
                        ) {
                            AppTimePicker(
                                calendarState = remember { mutableStateOf(state.reminder) },
                                themeResId = pickerDialogThemeResId,
                                onValueChange = onValueChange,
                            )
                        }
                        ReminderDivider()
                        ReminderControlRow(
                            iconResId = R.drawable.ic_repeat,
                            label = stringResource(id = R.string.reminder_repeat),
                        ) {
                            AppDropdownMenu(
                                itemToName = TaskViewState.CALENDAR_UNIT_TO_NAME,
                                selectedItemState = state.stateRepeatCalendarUnits,
                                onClickAdditional = onValueChange,
                            )
                        }
                    }
                }
            }
        }
        if (openTaskInfoDialogState.value) {
            TaskInfoDialog(
                createTime = state.created,
                onDismissClick = { openTaskInfoDialogState.value = false },
            )
        }
        if (openUnsavedTaskDialogState.value) {
            UnsavedTaskDialog(
                onDismiss = { openUnsavedTaskDialogState.value = false },
                onSaveTask = onSaveTask,
                onNavigateBack = onNavigateBack,
            )
        }
    }
}

@Composable
private fun ReminderControlRow(
    iconResId: Int,
    label: String,
    control: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.body1,
        )
        control()
    }
}

@Composable
private fun ReminderDivider() {
    Divider(
        modifier = Modifier.padding(start = 34.dp),
        color = AppTheme.glass.divider,
        thickness = 1.dp,
    )
}

@Preview
@Composable
private fun EditorScreenPreviewContent(
    @PreviewParameter(EditorScreenPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            EditorScreenContent(
                scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
                taskViewState = remember {
                    mutableStateOf(
                        TaskViewState(
                            title = mutableStateOf("Prepare release"),
                            description = mutableStateOf("Update changelog and publish build"),
                            isReminderSet = mutableStateOf(true),
                            color = mutableStateOf(AppTheme.taskColors[0]),
                        ),
                    )
                },
                openTaskInfoDialogState = remember { mutableStateOf(false) },
                openUnsavedTaskDialogState = remember { mutableStateOf(false) },
                valueChangedState = remember { mutableStateOf(true) },
                onNavigateBack = {},
                onSaveTask = {},
                onDeleteTask = {},
            )
        }
    }
}

private class EditorScreenPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
