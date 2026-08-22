package com.rustamft.tasksft.presentation.activity

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.usecase.GetPreferencesUseCase
import com.rustamft.tasksft.presentation.element.AppBackground
import com.rustamft.tasksft.presentation.global.SnackbarFlow
import com.rustamft.tasksft.presentation.theme.AppTheme
import org.koin.compose.koinInject

@Composable
internal fun MainActivityContent(
    context: Context = LocalContext.current,
    snackbarFlow: SnackbarFlow = koinInject(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    getPreferencesUseCase: GetPreferencesUseCase = koinInject(),
) {
    val preferences by getPreferencesUseCase
        .execute()
        .collectAsStateWithLifecycle(initialValue = Preferences())

    LaunchedEffect(snackbarFlow, snackbarHostState) {
        snackbarFlow.collect { uiText ->
            snackbarHostState.showSnackbar(message = uiText.asString(context))
        }
    }

    AppTheme(theme = preferences.theme) {
        AppBackground {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController,
                dependenciesContainerBuilder = {
                    dependency(snackbarHostState)
                },
            )
        }
    }
}
