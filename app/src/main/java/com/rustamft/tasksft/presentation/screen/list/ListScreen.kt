package com.rustamft.tasksft.presentation.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.EditorScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.dialog.AppInfoDialog
import com.rustamft.tasksft.presentation.element.AppBackground
import com.rustamft.tasksft.presentation.element.AppSnackbarHost
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.global.GITHUB_LINK
import com.rustamft.tasksft.presentation.global.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.global.ROUTE_LIST
import com.rustamft.tasksft.presentation.global.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_FAB
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CARD
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CHECKBOX
import com.rustamft.tasksft.presentation.global.toDateTime
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.screen.editor.model.ReminderRepeat
import com.rustamft.tasksft.presentation.screen.list.model.ListUiState
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.appPressable
import com.rustamft.tasksft.presentation.theme.appToggleable
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Destination<RootGraph>(start = true, route = ROUTE_LIST)
@Composable
internal fun ListScreen(
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState,
    viewModel: ListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAppInfo by rememberSaveable { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    ListScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onOpenSettings = { navigator.navigate(Direction(ROUTE_SETTINGS)) },
        onNewTask = { navigator.navigate(Direction(ROUTE_EDITOR)) },
        onEditTask = { id -> navigator.navigate(EditorScreenDestination(taskId = id)) },
        onDeleteFinishedTasks = viewModel::deleteFinishedTasks,
        onSetTaskFinished = viewModel::setTaskFinished,
        onShowAppInfo = { showAppInfo = true },
    )
    if (showAppInfo) {
        AppInfoDialog(
            onDismiss = { showAppInfo = false },
            onOpenGithub = { uriHandler.openUri(GITHUB_LINK.toUri().toString()) },
        )
    }
}

@Composable
internal fun ListScreenContent(
    uiState: ListUiState,
    snackbarHostState: SnackbarHostState,
    onOpenSettings: () -> Unit,
    onNewTask: () -> Unit,
    onEditTask: (Int) -> Unit,
    onDeleteFinishedTasks: () -> Unit,
    onSetTaskFinished: (Task, Boolean) -> Unit,
    onShowAppInfo: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_LIST_SCREEN),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
        ),
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBar(
                title = stringResource(R.string.screen_tasks),
                leadingItem = NavItem(
                    painterResId = R.drawable.ic_tasks,
                    descriptionResId = R.string.app_name,
                    enabled = false,
                ),
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_settings,
                        descriptionResId = R.string.action_settings,
                        onClick = onOpenSettings,
                    ),
                ),
                dropdownItems = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_clean,
                        descriptionResId = R.string.action_delete_finished,
                        onClick = onDeleteFinishedTasks,
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.app_info,
                        onClick = onShowAppInfo,
                    ),
                ),
            )
        },
        floatingActionButton = {
            Fab(
                modifier = Modifier.testTag(TAG_LIST_SCREEN_FAB),
                item = NavItem(
                    painterResId = R.drawable.ic_add,
                    descriptionResId = R.string.action_new_task,
                    onClick = onNewTask,
                ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 4.dp,
                end = 16.dp,
                bottom = 116.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = uiState.tasks,
                key = Task::id,
            ) { task ->
                TaskCard(
                    task = task,
                    onOpen = { onEditTask(task.id) },
                    onFinishedChange = { finished -> onSetTaskFinished(task, finished) },
                )
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: Task,
    onOpen: () -> Unit,
    onFinishedChange: (Boolean) -> Unit,
) {
    val accent = when {
        task.finished -> AppTheme.glass.contentMuted
        else -> AppTheme.taskColor(task.colorIndex)
    }
    val contentColor = when {
        task.finished -> AppTheme.glass.contentMuted
        else -> AppTheme.glass.content
    }
    val finishedDescription = stringResource(R.string.task_finished_state)
    AppSurface(
        modifier = Modifier
            .fillMaxWidth()
            .appPressable(onClick = onOpen, pressedScale = 0.99f)
            .testTag(TAG_LIST_SCREEN_TASK_CARD),
        elevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            accent.copy(alpha = if (task.finished) 0.05f else 0.16f),
                            Color.Transparent,
                        ),
                    ),
                )
                .heightIn(min = 92.dp)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .appToggleable(
                            value = task.finished,
                            role = Role.Checkbox,
                            onValueChange = onFinishedChange,
                        )
                        .semantics { contentDescription = finishedDescription }
                        .testTag(TAG_LIST_SCREEN_TASK_CHECKBOX)
                        .padding(4.dp)
                        .border(2.dp, accent, RoundedCornerShape(9.dp))
                        .background(
                            if (task.finished) accent else Color.Transparent,
                            RoundedCornerShape(9.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (task.finished) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(R.drawable.ic_done),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        color = contentColor,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (task.finished) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                        maxLines = 2,
                    )
                    if (task.reminder != 0L) {
                        Spacer(modifier = Modifier.height(6.dp))
                        TaskMetadata(task = task, accent = accent)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = AppTheme.glass.contentMuted,
                )
            }
        }
    }
}

@Composable
private fun TaskMetadata(task: Task, accent: Color) {
    val dateTime = task.reminder.toDateTime()
    val repeat = ReminderRepeat.entries.firstOrNull {
        it.calendarUnit == task.repeatCalendarUnit && it != ReminderRepeat.NONE
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_time),
            contentDescription = null,
            tint = accent,
        )
        Text(
            text = task.reminder.reminderText(dateTime.date, dateTime.time),
            color = accent,
            style = MaterialTheme.typography.bodySmall,
        )
        if (repeat != null) {
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = null,
                tint = accent,
            )
            Text(
                text = when (repeat) {
                    ReminderRepeat.DAILY -> stringResource(R.string.reminder_daily)
                    ReminderRepeat.WEEKLY -> stringResource(R.string.reminder_weekly)
                    ReminderRepeat.MONTHLY -> stringResource(R.string.reminder_monthly)
                    ReminderRepeat.NONE -> error("One-time reminders have no repeat metadata")
                },
                color = accent,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun Long.reminderText(date: String, time: String): String {
    val target = Calendar.getInstance().apply { timeInMillis = this@reminderText }
    val today = Calendar.getInstance()
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    return when {
        target.isSameDay(today) -> stringResource(R.string.reminder_today_at, time)
        target.isSameDay(tomorrow) -> stringResource(R.string.reminder_tomorrow_at, time)
        else -> "$date $time"
    }
}

private fun Calendar.isSameDay(other: Calendar): Boolean =
    get(Calendar.ERA) == other.get(Calendar.ERA) &&
            get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

@Preview
@Composable
private fun ListScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            ListScreenContent(
                uiState = ListUiState(
                    tasks = listOf("Prepare release", "Buy groceries", "Call Mom", "Book hotel").mapIndexed { index, title ->
                        Task(
                            id = index,
                            created = 0L,
                            title = title,
                            description = "",
                            reminder = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_MONTH, index)
                            }.timeInMillis,
                            repeatCalendarUnit = if (index == 3) Calendar.WEEK_OF_MONTH else 0,
                            finished = index == 3,
                            colorIndex = index,
                        )
                    },
                ),
                snackbarHostState = SnackbarHostState(),
                onOpenSettings = {},
                onNewTask = {},
                onEditTask = {},
                onDeleteFinishedTasks = {},
                onSetTaskFinished = { _, _ -> },
                onShowAppInfo = {},
            )
        }
    }
}
