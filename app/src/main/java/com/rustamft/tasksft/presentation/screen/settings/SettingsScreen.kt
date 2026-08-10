package com.rustamft.tasksft.presentation.screen.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.dialog.ExportConfirmDialog
import com.rustamft.tasksft.presentation.element.AppBackground
import com.rustamft.tasksft.presentation.element.AppIconButton
import com.rustamft.tasksft.presentation.element.AppSnackbarHost
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.appThemeControl
import com.rustamft.tasksft.presentation.global.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_EXPORT
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_RESTORE
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_CONTROL
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(route = ROUTE_SETTINGS)
@Composable
internal fun SettingsScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.exportTasks(directoryUri = uri) }
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.importTasks(fileUri = uri) }
        }
    }
    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }
    SettingsScreenContent(
        scaffoldState = scaffoldState,
        preferencesState = viewModel.preferencesFlow.collectAsState(initial = Preferences()),
        openExportConfirmDialogState = viewModel.openExportConfirmDialogState,
        onNavigateBack = { navigator.popBackStack() },
        onSetTheme = { theme -> viewModel.setTheme(theme) },
        onChooseDirectory = {
            exportLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        },
        onChooseFile = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/*"
            }
            importLauncher.launch(intent)
        },
        onExportTasks = { uri -> viewModel.exportTasks(uri) },
    )
}

@Composable
private fun SettingsScreenContent(
    scaffoldState: ScaffoldState,
    preferencesState: State<Preferences>,
    openExportConfirmDialogState: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onSetTheme: (Preferences.Theme) -> Unit,
    onChooseDirectory: () -> Unit,
    onChooseFile: () -> Unit,
    onExportTasks: (Uri) -> Unit,
) {
    val preferences by preferencesState
    val nextTheme = when (preferences.theme) {
        is Preferences.Theme.Auto -> Preferences.Theme.Light
        is Preferences.Theme.Light -> Preferences.Theme.Dark
        is Preferences.Theme.Dark -> Preferences.Theme.Auto
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_SETTINGS_SCREEN),
        scaffoldState = scaffoldState,
        snackbarHost = { AppSnackbarHost(hostState = it) },
        backgroundColor = Color.Transparent,
        topBar = {
            TopBar(
                title = stringResource(id = R.string.action_settings),
                items = emptyList(),
                backButton = {
                    AppIconButton(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        tint = AppTheme.glass.content,
                        onClick = onNavigateBack,
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            AppSurface(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(
                        iconResId = themeIcon(preferences.theme),
                        title = stringResource(id = R.string.appearance),
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    ThemeCycleControl(
                        modifier = Modifier.testTag(TAG_SETTINGS_THEME_CONTROL),
                        selectedTheme = preferences.theme,
                        onClick = { onSetTheme(nextTheme) },
                    )
                }
            }
            AppSurface(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(
                        iconResId = R.drawable.ic_save,
                        title = stringResource(id = R.string.backup),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    SettingsActionRow(
                        modifier = Modifier.testTag(TAG_SETTINGS_EXPORT),
                        iconResId = R.drawable.ic_save,
                        label = stringResource(id = R.string.action_export_tasks),
                        onClick = {
                            if (preferences.backupDirectory.isEmpty()) {
                                onChooseDirectory()
                            } else {
                                openExportConfirmDialogState.value = true
                            }
                        },
                    )
                    Divider(
                        modifier = Modifier.padding(start = 40.dp),
                        color = AppTheme.glass.divider,
                    )
                    SettingsActionRow(
                        modifier = Modifier.testTag(TAG_SETTINGS_RESTORE),
                        iconResId = R.drawable.ic_restore,
                        label = stringResource(id = R.string.action_import_tasks),
                        onClick = onChooseFile,
                    )
                }
            }
        }
    }
    if (openExportConfirmDialogState.value) {
        ExportConfirmDialog(
            backupDirectory = preferences.backupDirectory,
            onDismissClick = { openExportConfirmDialogState.value = false },
            onChooseDirectoryClick = onChooseDirectory,
            onExportTasksClick = { onExportTasks(preferences.backupDirectory.toUri()) },
        )
    }
}

@Composable
private fun SectionTitle(
    iconResId: Int,
    title: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = AppTheme.glass.accent,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
private fun ThemeCycleControl(
    modifier: Modifier = Modifier,
    selectedTheme: Preferences.Theme,
    onClick: () -> Unit,
) {
    val options = listOf(
        Preferences.Theme.Auto to stringResource(id = R.string.theme_auto_short),
        Preferences.Theme.Light to stringResource(id = R.string.theme_light_short),
        Preferences.Theme.Dark to stringResource(id = R.string.theme_dark_short),
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .appThemeControl(shape = AppControlShape)
            .clip(AppControlShape)
            .clickable(onClick = onClick)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEach { (theme, label) ->
            val selected = selectedTheme::class == theme::class
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (selected) AppTheme.glass.accent else Color.Transparent,
                    )
                    .padding(horizontal = 6.dp, vertical = 11.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    color = if (selected) Color.White else AppTheme.glass.content,
                    style = MaterialTheme.typography.button,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun SettingsActionRow(
    modifier: Modifier = Modifier,
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppControlShape)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.body1,
        )
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
    }
}

private fun themeIcon(theme: Preferences.Theme): Int = when (theme) {
    is Preferences.Theme.Auto -> R.drawable.ic_theme_auto
    is Preferences.Theme.Light -> R.drawable.ic_theme_light
    is Preferences.Theme.Dark -> R.drawable.ic_theme_dark
}

@Preview
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            SettingsScreenContent(
                scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
                preferencesState = remember { mutableStateOf(Preferences()) },
                openExportConfirmDialogState = remember { mutableStateOf(false) },
                onNavigateBack = {},
                onSetTheme = {},
                onChooseDirectory = {},
                onChooseFile = {},
                onExportTasks = {},
            )
        }
    }
}

private class SettingsScreenPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
