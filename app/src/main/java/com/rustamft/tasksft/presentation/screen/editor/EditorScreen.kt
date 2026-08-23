package com.rustamft.tasksft.presentation.screen.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.rustamft.tasksft.presentation.element.AppSnackbarHost
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.AppTimePicker
import com.rustamft.tasksft.presentation.global.DEEP_LINK_URI
import com.rustamft.tasksft.presentation.global.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_COLOR
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_EDITTEXT_TITLE
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_FAB
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_REMINDER_SWITCH
import com.rustamft.tasksft.presentation.global.TAG_EDITOR_SCREEN_REPEAT
import com.rustamft.tasksft.presentation.model.UIText
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.screen.editor.model.EditorEffect
import com.rustamft.tasksft.presentation.screen.editor.model.EditorUiState
import com.rustamft.tasksft.presentation.screen.editor.model.ReminderRepeat
import com.rustamft.tasksft.presentation.screen.editor.model.defaultReminderEpochMillis
import com.rustamft.tasksft.presentation.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val reminderRepeatNames = mapOf(
    ReminderRepeat.NONE to UIText.StringResource(R.string.reminder_one_time),
    ReminderRepeat.DAILY to UIText.StringResource(R.string.reminder_daily),
    ReminderRepeat.WEEKLY to UIText.StringResource(R.string.reminder_weekly),
    ReminderRepeat.MONTHLY to UIText.StringResource(R.string.reminder_monthly),
)

@Destination<RootGraph>(
    route = ROUTE_EDITOR,
    deepLinks = [DeepLink(uriPattern = "$DEEP_LINK_URI$FULL_ROUTE_PLACEHOLDER")],
)
@Composable
internal fun EditorScreen(
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState,
    taskId: Int?,
    viewModel: EditorViewModel = koinViewModel(parameters = { parametersOf(taskId) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showTaskInfo by rememberSaveable { mutableStateOf(false) }
    var showUnsavedTask by rememberSaveable { mutableStateOf(false) }
    var repeatExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel, navigator) {
        viewModel.effects.collect { effect ->
            when (effect) {
                EditorEffect.NavigateBack -> navigator.popBackStack()
            }
        }
    }
    val requestBack: () -> Unit = {
        when {
            uiState.saveEnabled -> showUnsavedTask = true
            else -> navigator.popBackStack()
        }
    }
    BackHandler(onBack = requestBack)
    EditorScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        repeatExpanded = repeatExpanded,
        onRepeatExpandedChange = { repeatExpanded = it },
        onNavigateBack = requestBack,
        onShowInfo = { showTaskInfo = true },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onColorChange = viewModel::onColorChange,
        onReminderEnabledChange = viewModel::onReminderEnabledChange,
        onReminderChange = viewModel::onReminderChange,
        onReminderRepeatChange = viewModel::onReminderRepeatChange,
        onSave = viewModel::saveTask,
        onDelete = viewModel::deleteTask,
    )
    if (showTaskInfo) {
        TaskInfoDialog(
            createTime = uiState.created,
            onDismissClick = { showTaskInfo = false },
        )
    }
    if (showUnsavedTask) {
        UnsavedTaskDialog(
            saveEnabled = uiState.saveEnabled,
            onDismiss = { showUnsavedTask = false },
            onSaveTask = viewModel::saveTask,
            onNavigateBack = { navigator.popBackStack() },
        )
    }
}

@Composable
internal fun EditorScreenContent(
    uiState: EditorUiState,
    snackbarHostState: SnackbarHostState,
    repeatExpanded: Boolean,
    onRepeatExpandedChange: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onShowInfo: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onColorChange: (Int) -> Unit,
    onReminderEnabledChange: (Boolean) -> Unit,
    onReminderChange: (Long) -> Unit,
    onReminderRepeatChange: (ReminderRepeat) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    val fieldColors = TextFieldDefaults.colors(
        focusedTextColor = AppTheme.glass.content,
        unfocusedTextColor = AppTheme.glass.content,
        focusedLabelColor = AppTheme.glass.accent,
        unfocusedLabelColor = AppTheme.glass.contentMuted,
        cursorColor = AppTheme.glass.accent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = AppTheme.glass.accent,
        unfocusedIndicatorColor = AppTheme.glass.divider,
        disabledIndicatorColor = AppTheme.glass.divider,
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_EDITOR_SCREEN),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
        ),
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBar(
                title = stringResource(R.string.screen_edit_task),
                leadingItem = NavItem(
                    painterResId = R.drawable.ic_arrow_back,
                    descriptionResId = R.string.action_back,
                    onClick = onNavigateBack,
                ),
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_delete,
                        descriptionResId = R.string.action_delete,
                        onClick = onDelete,
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.task_info,
                        onClick = onShowInfo,
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
                    onClick = onSave,
                ),
                visible = uiState.saveEnabled,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 124.dp),
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TAG_EDITOR_SCREEN_EDITTEXT_TITLE),
                value = uiState.title,
                onValueChange = onTitleChange,
                label = { Text(text = stringResource(R.string.task_title)) },
                singleLine = true,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = fieldColors,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.description,
                onValueChange = onDescriptionChange,
                label = { Text(text = stringResource(R.string.task_description)) },
                maxLines = 10,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = fieldColors,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.task_color),
                color = AppTheme.glass.contentMuted,
                style = MaterialTheme.typography.bodySmall,
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
                        selected = uiState.colorIndex == index,
                        contentDescription = stringResource(R.string.task_color_option, index + 1),
                        onClick = { onColorChange(index) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            ReminderSection(
                uiState = uiState,
                repeatExpanded = repeatExpanded,
                onRepeatExpandedChange = onRepeatExpandedChange,
                onReminderEnabledChange = onReminderEnabledChange,
                onReminderChange = onReminderChange,
                onReminderRepeatChange = onReminderRepeatChange,
            )
        }
    }
}

@Composable
private fun ReminderSection(
    uiState: EditorUiState,
    repeatExpanded: Boolean,
    onRepeatExpandedChange: (Boolean) -> Unit,
    onReminderEnabledChange: (Boolean) -> Unit,
    onReminderChange: (Long) -> Unit,
    onReminderRepeatChange: (ReminderRepeat) -> Unit,
) {
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
                    painter = painterResource(R.drawable.ic_reminder),
                    contentDescription = null,
                    tint = AppTheme.glass.accent,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.reminder),
                    color = AppTheme.glass.content,
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(
                    modifier = Modifier.testTag(TAG_EDITOR_SCREEN_REMINDER_SWITCH),
                    checked = uiState.reminderEnabled,
                    onCheckedChange = onReminderEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AppTheme.glass.accent,
                        uncheckedThumbColor = AppTheme.glass.contentMuted,
                        uncheckedTrackColor = AppTheme.glass.control,
                        uncheckedBorderColor = AppTheme.glass.divider,
                    ),
                )
            }
            if (uiState.reminderEnabled) {
                ReminderDivider()
                ReminderControlRow(
                    iconResId = R.drawable.ic_event,
                    label = stringResource(R.string.reminder_date),
                    control = {
                        AppDatePicker(
                            value = uiState.reminderEpochMillis,
                            onValueChange = onReminderChange,
                        )
                    },
                )
                ReminderDivider()
                ReminderControlRow(
                    iconResId = R.drawable.ic_time,
                    label = stringResource(R.string.reminder_time),
                ) {
                    AppTimePicker(
                        value = uiState.reminderEpochMillis,
                        onValueChange = onReminderChange,
                    )
                }
                ReminderDivider()
                ReminderControlRow(
                    iconResId = R.drawable.ic_repeat,
                    label = stringResource(R.string.reminder_repeat),
                ) {
                    AppDropdownMenu(
                        modifier = Modifier.testTag(TAG_EDITOR_SCREEN_REPEAT),
                        itemToName = reminderRepeatNames,
                        value = uiState.reminderRepeat,
                        expanded = repeatExpanded,
                        onValueChange = onReminderRepeatChange,
                        onExpandedChange = onRepeatExpandedChange,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderControlRow(
    modifier: Modifier = Modifier,
    iconResId: Int,
    label: String,
    control: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.bodyLarge,
        )
        control()
    }
}

@Composable
private fun ReminderDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 34.dp),
        color = AppTheme.glass.divider,
        thickness = 1.dp,
    )
}

@Preview
@Composable
private fun EditorScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            EditorScreenContent(
                uiState = EditorUiState(
                    id = 0,
                    created = 0,
                    title = "Prepare release",
                    description = "Update changelog and publish build",
                    reminderEnabled = true,
                    reminderEpochMillis = defaultReminderEpochMillis(),
                    reminderRepeat = ReminderRepeat.DAILY,
                    finished = false,
                    colorIndex = AppTheme.taskColors.indices.first,
                    saveEnabled = true,
                ),
                snackbarHostState = SnackbarHostState(),
                repeatExpanded = false,
                onRepeatExpandedChange = {},
                onNavigateBack = {},
                onShowInfo = {},
                onTitleChange = {},
                onDescriptionChange = {},
                onColorChange = {},
                onReminderEnabledChange = {},
                onReminderChange = {},
                onReminderRepeatChange = {},
                onSave = {},
                onDelete = {},
            )
        }
    }
}
