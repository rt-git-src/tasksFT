package com.rustamft.tasksft.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone
import com.rustamft.tasksft.presentation.theme.glassSurface

@Composable
internal fun Fab(
    modifier: Modifier = Modifier,
    item: NavItem,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier
                .glassSurface(
                    shape = CircleShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                )
                .alpha(if (item.enabled) 1f else 0.55f)
                .semantics { if (!item.enabled) disabled() },
            icon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    AppTheme.glass.accent.copy(alpha = 0.86f),
                                    AppTheme.colors.primaryContainer,
                                ),
                            ),
                            CircleShape,
                        )
                        .border(1.dp, AppTheme.glass.rimTop, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(item.painterResId),
                        contentDescription = stringResource(item.descriptionResId),
                        tint = Color.White,
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(item.descriptionResId),
                    color = AppTheme.glass.accent,
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            shape = CircleShape,
            containerColor = Color.Transparent,
            contentColor = AppTheme.glass.accent,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
            ),
            onClick = { if (item.enabled) item.onClick() },
        )
    }
}

@Preview
@Composable
private fun FabPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        Fab(
            item = NavItem(
                painterResId = R.drawable.ic_save,
                descriptionResId = R.string.action_save,
                onClick = {},
            ),
        )
    }
}
