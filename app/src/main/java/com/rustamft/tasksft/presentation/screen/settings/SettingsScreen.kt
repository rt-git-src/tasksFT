package com.rustamft.tasksft.presentation.screen.settings

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.dialog.ExportConfirmDialog
import com.rustamft.tasksft.presentation.element.AppBackground
import com.rustamft.tasksft.presentation.element.AppSnackbarHost
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.global.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_EXPORT
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_RESTORE
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_CONTROL
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_DARK
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_LIGHT
import com.rustamft.tasksft.presentation.global.TAG_SETTINGS_THEME_SYSTEM
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.screen.settings.model.SettingsEffect
import com.rustamft.tasksft.presentation.screen.settings.model.SettingsUiState
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.appPressable
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(route = ROUTE_SETTINGS)
@Composable
internal fun SettingsScreen(
    navigator: DestinationsNavigator,
    snackbarHostState: SnackbarHostState,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showExportConfirmation by rememberSaveable { mutableStateOf(false) }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.toString()?.let(viewModel::exportTasks)
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.toString()?.let(viewModel::importTasks)
        }
    }
    LaunchedEffect(viewModel, navigator) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SettingsEffect.NavigateBack -> navigator.popBackStack()
            }
        }
    }
    val chooseDirectory = {
        exportLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
    }
    SettingsScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateBack = { navigator.popBackStack() },
        onThemeSelected = viewModel::setTheme,
        onChooseDirectory = chooseDirectory,
        onChooseFile = {
            importLauncher.launch(
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/*"
                },
            )
        },
        onShowExportConfirmation = { showExportConfirmation = true },
    )
    if (showExportConfirmation) {
        ExportConfirmDialog(
            backupDirectory = uiState.preferences.backupDirectory,
            onDismiss = { showExportConfirmation = false },
            onChooseDirectory = chooseDirectory,
            onExportTasks = {
                viewModel.exportTasks(uiState.preferences.backupDirectory)
            },
        )
    }
}

@Composable
internal fun SettingsScreenContent(
    uiState: SettingsUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onThemeSelected: (Preferences.Theme) -> Unit,
    onChooseDirectory: () -> Unit,
    onChooseFile: () -> Unit,
    onShowExportConfirmation: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_SETTINGS_SCREEN),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
        ),
        snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                title = stringResource(R.string.action_settings),
                leadingItem = NavItem(
                    painterResId = R.drawable.ic_arrow_back,
                    descriptionResId = R.string.action_back,
                    onClick = onNavigateBack,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            AppSurface(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(
                        iconResId = R.drawable.ic_appearance,
                        title = stringResource(R.string.appearance),
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    ThemeSelector(
                        modifier = Modifier.testTag(TAG_SETTINGS_THEME_CONTROL),
                        selectedTheme = uiState.preferences.theme,
                        onThemeSelected = onThemeSelected,
                    )
                }
            }
            AppSurface(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionTitle(
                        iconResId = R.drawable.ic_backup,
                        title = stringResource(R.string.backup),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    SettingsActionRow(
                        modifier = Modifier.testTag(TAG_SETTINGS_EXPORT),
                        iconResId = R.drawable.ic_export,
                        label = stringResource(R.string.action_export_tasks),
                        onClick = {
                            if (uiState.preferences.backupDirectory.isEmpty()) {
                                onChooseDirectory()
                            } else {
                                onShowExportConfirmation()
                            }
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 40.dp),
                        color = AppTheme.glass.divider,
                    )
                    SettingsActionRow(
                        modifier = Modifier.testTag(TAG_SETTINGS_RESTORE),
                        iconResId = R.drawable.ic_import,
                        label = stringResource(R.string.action_import_tasks),
                        onClick = onChooseFile,
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(iconResId: Int, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = AppTheme.glass.accent,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSelector(
    modifier: Modifier = Modifier,
    selectedTheme: Preferences.Theme,
    onThemeSelected: (Preferences.Theme) -> Unit,
) {
    val options = listOf(
        Triple(Preferences.Theme.Auto, stringResource(R.string.theme_auto_short), TAG_SETTINGS_THEME_SYSTEM),
        Triple(Preferences.Theme.Light, stringResource(R.string.theme_light_short), TAG_SETTINGS_THEME_LIGHT),
        Triple(Preferences.Theme.Dark, stringResource(R.string.theme_dark_short), TAG_SETTINGS_THEME_DARK),
    )
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
    ) {
        options.forEachIndexed { index, (theme, label, tag) ->
            val selected = selectedTheme == theme
            SegmentedButton(
                modifier = Modifier.testTag(tag),
                selected = selected,
                onClick = { if (!selected) onThemeSelected(theme) },
                shape = SegmentedButtonDefaults.itemShape(index, options.size, AppControlShape),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AppTheme.glass.accent,
                    activeContentColor = Color.White,
                    activeBorderColor = AppTheme.glass.rimTop,
                    inactiveContainerColor = Color.Transparent,
                    inactiveContentColor = AppTheme.glass.content,
                    inactiveBorderColor = AppTheme.glass.divider,
                ),
                icon = {},
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun SettingsActionRow(
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appPressable(
                shape = AppControlShape,
                pressedScale = 0.99f,
                onClick = onClick,
            )
            .padding(horizontal = 8.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.bodyLarge,
        )
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppBackground {
            SettingsScreenContent(
                uiState = SettingsUiState(Preferences(theme = theme)),
                snackbarHostState = SnackbarHostState(),
                onNavigateBack = {},
                onThemeSelected = {},
                onChooseDirectory = {},
                onChooseFile = {},
                onShowExportConfirmation = {},
            )
        }
    }
}
