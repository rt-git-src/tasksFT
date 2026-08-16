package com.rustamft.tasksft.presentation.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
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
import com.rustamft.tasksft.presentation.element.appPressable
import com.rustamft.tasksft.presentation.element.appToggleable
import com.rustamft.tasksft.presentation.global.GITHUB_LINK
import com.rustamft.tasksft.presentation.global.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.global.ROUTE_LIST
import com.rustamft.tasksft.presentation.global.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_FAB
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CARD
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CHECKBOX
import com.rustamft.tasksft.presentation.global.toDateTime
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.AppTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Destination<RootGraph>(start = true, route = ROUTE_LIST)
@Composable
internal fun ListScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    viewModel: ListViewModel = koinViewModel(),
) {
    val listOfTasksState = viewModel.listOfTasksFlow.collectAsState(initial = emptyList())
    ListScreenContent(
        scaffoldState = scaffoldState,
        tasks = listOfTasksState,
        openAppInfoDialog = viewModel.openAppInfoDialogState,
        onNavigateToSettings = { navigator.navigate(Direction(ROUTE_SETTINGS)) },
        onNavigateToEditorNewTask = { navigator.navigate(Direction(ROUTE_EDITOR)) },
        onNavigateToEditorExistingTask = { id ->
            navigator.navigate(EditorScreenDestination(taskId = id))
        },
        onDeleteFinishedTasks = {
            viewModel.deleteTasks(tasks = listOfTasksState.value.filter { it.finished })
        },
        onFinishTask = viewModel::saveTask,
    )
}

@Composable
private fun ListScreenContent(
    scaffoldState: ScaffoldState,
    tasks: State<List<Task>>,
    openAppInfoDialog: MutableState<Boolean>,
    onNavigateToSettings: () -> Unit,
    onNavigateToEditorNewTask: () -> Unit,
    onNavigateToEditorExistingTask: (Int) -> Unit,
    onDeleteFinishedTasks: () -> Unit,
    onFinishTask: (Task) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_LIST_SCREEN),
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackbarHost(hostState = it) },
        backgroundColor = Color.Transparent,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.screen_tasks),
                leadingIconResId = R.drawable.ic_tasks,
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_settings,
                        descriptionResId = R.string.action_settings,
                        onClick = onNavigateToSettings,
                    ),
                ),
                overflowItems = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_clean,
                        descriptionResId = R.string.action_delete_finished,
                        onClick = onDeleteFinishedTasks,
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.app_info,
                        onClick = { openAppInfoDialog.value = true },
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
                    onClick = onNavigateToEditorNewTask,
                ),
            )
        },
    ) { paddingValues ->
        val uriHandler = LocalUriHandler.current
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 4.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 116.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = tasks.value,
                key = { it.id },
            ) { task ->
                TaskCard(
                    task = task,
                    onOpen = { onNavigateToEditorExistingTask(task.id) },
                    onFinishedChange = { finished ->
                        onFinishTask(task.copy(finished = finished))
                    },
                )
            }
        }
        if (openAppInfoDialog.value) {
            AppInfoDialog(
                onDismiss = { openAppInfoDialog.value = false },
                onOpenGithub = { uriHandler.openUri(GITHUB_LINK.toUri().toString()) },
            )
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
        else -> Color(task.color).copy(alpha = 1f)
    }
    val contentColor = when {
        task.finished -> AppTheme.glass.contentMuted
        else -> AppTheme.glass.content
    }
    val finishedDescription = stringResource(id = R.string.task_finished_state)
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
                        colors = listOf(
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
                        .semantics {
                            contentDescription = finishedDescription
                        }
                        .testTag(TAG_LIST_SCREEN_TASK_CHECKBOX)
                        .padding(4.dp)
                        .border(
                            width = 2.dp,
                            color = accent,
                            shape = RoundedCornerShape(9.dp),
                        )
                        .background(
                            color = if (task.finished) accent else Color.Transparent,
                            shape = RoundedCornerShape(9.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (task.finished) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = R.drawable.ic_done),
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
                        style = MaterialTheme.typography.subtitle1,
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
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = AppTheme.glass.contentMuted,
                )
            }
        }
    }
}

@Composable
private fun TaskMetadata(
    task: Task,
    accent: Color,
) {
    val dateTime = task.reminder.toDateTime()
    val reminderText = task.reminder.reminderText(dateTime.date, dateTime.time)
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = null,
            tint = accent,
        )
        Text(
            text = reminderText,
            color = accent,
            style = MaterialTheme.typography.caption,
        )
        if (task.repeatCalendarUnit != 0) {
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_repeat),
                contentDescription = null,
                tint = accent,
            )
            TaskViewState.CALENDAR_UNIT_TO_NAME[task.repeatCalendarUnit]?.let { unitName ->
                Text(
                    text = unitName.asString(),
                    color = accent,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
private fun Long.reminderText(date: String, time: String): String {
    val target = Calendar.getInstance().apply { timeInMillis = this@reminderText }
    val today = Calendar.getInstance()
    val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    return when {
        target.isSameDay(today) -> stringResource(id = R.string.reminder_today_at, time)
        target.isSameDay(tomorrow) -> stringResource(id = R.string.reminder_tomorrow_at, time)
        else -> "$date $time"
    }
}

private fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.ERA) == other.get(Calendar.ERA)
            && get(Calendar.YEAR) == other.get(Calendar.YEAR)
            && get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}

@Preview
@Composable
private fun ListScreenPreview(
    @PreviewParameter(ListScreenPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            ListScreenContent(
                scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
                tasks = remember {
                    mutableStateOf(
                        listOf(
                            "Prepare release" to "Update changelog and publish build",
                            "Buy groceries" to "",
                            "Call Mom" to "",
                            "Book hotel" to "",
                        ).mapIndexed { index, (title, description) ->
                            Task(
                                id = index,
                                created = 0L,
                                title = title,
                                description = description,
                                reminder = Calendar
                                    .getInstance()
                                    .apply {
                                        add(Calendar.DAY_OF_MONTH, index)
                                        add(Calendar.HOUR_OF_DAY, index)
                                    }.timeInMillis,
                                repeatCalendarUnit = if (index == 3) Calendar.WEEK_OF_MONTH else 0,
                                finished = index == 3,
                                color = AppTheme.taskColors[index].toArgb(),
                            )
                        },
                    )
                },
                openAppInfoDialog = remember { mutableStateOf(false) },
                onNavigateToSettings = {},
                onNavigateToEditorNewTask = {},
                onNavigateToEditorExistingTask = {},
                onDeleteFinishedTasks = {},
                onFinishTask = {},
            )
        }
    }
}

private class ListScreenPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
