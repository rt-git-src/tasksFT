package com.rustamft.tasksft.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.GlassTone
import com.rustamft.tasksft.presentation.element.appPressable
import com.rustamft.tasksft.presentation.element.appTheme
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun Fab(
    modifier: Modifier = Modifier,
    item: NavItem,
    visibilityState: State<Boolean> = remember { mutableStateOf(true) },
) {
    AnimatedVisibility(
        visible = visibilityState.value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
        ) {
            Box(
                modifier = modifier
                    .width(248.dp)
                    .height(68.dp)
                    .appPressable(onClick = item.onClick)
                    .appTheme(
                        shape = CircleShape,
                        tone = GlassTone.Strong,
                        elevation = 12.dp,
                    )
                    .padding(horizontal = 10.dp, vertical = 9.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    AppTheme.glass.accent.copy(alpha = 0.86f),
                                    AppTheme.colors.primaryVariant,
                                ),
                            ),
                        )
                        .border(1.dp, AppTheme.glass.rimTop, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = item.painterResId),
                        contentDescription = stringResource(id = item.descriptionResId),
                        tint = AppTheme.colors.secondaryVariant,
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 54.dp, vertical = 15.dp),
                    text = stringResource(item.descriptionResId),
                    textAlign = TextAlign.Center,
                    color = AppTheme.glass.accent,
                    style = MaterialTheme.typography.button,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FabPreview(
    @PreviewParameter(FabPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        Fab(
            modifier = Modifier.width(250.dp),
            item = NavItem(
                painterResId = R.drawable.ic_save,
                descriptionResId = R.string.action_save,
                onClick = {},
            ),
        )
    }
}

private class FabPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
