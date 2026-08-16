package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.AppIconButton
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.GlassTone
import com.rustamft.tasksft.presentation.element.appTheme
import com.rustamft.tasksft.presentation.element.appThemeControl
import com.rustamft.tasksft.presentation.theme.AppBarShape
import com.rustamft.tasksft.presentation.theme.AppCardShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun TopBar(
    title: String,
    backButton: (@Composable () -> Unit)? = null,
    leadingItem: NavItem? = null,
    leadingIconResId: Int? = null,
    items: List<NavItem>,
    overflowItems: List<NavItem> = emptyList(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        AppSurface(
            modifier = if (items.isEmpty() && overflowItems.isEmpty()) {
                Modifier
                    .fillMaxWidth(0.72f)
                    .widthIn(min = 224.dp)
            } else {
                Modifier.fillMaxWidth()
            },
            shape = AppBarShape,
            tone = GlassTone.Strong,
            elevation = 12.dp,
            contentPadding = PaddingValues(5.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when {
                    backButton != null -> TopBarIconWell(content = backButton)
                    leadingItem != null -> TopBarIcon(item = leadingItem)
                    leadingIconResId != null -> TopBarIconWell {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = leadingIconResId),
                            contentDescription = null,
                            tint = AppTheme.glass.accent,
                        )
                    }
                }

                if (backButton != null || leadingItem != null || leadingIconResId != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    color = AppTheme.glass.content,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items.forEach { item ->
                        TopBarIcon(item = item)
                    }
                    if (overflowItems.isNotEmpty()) {
                        TopBarOverflow(items = overflowItems)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBarOverflow(items: List<NavItem>) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TopBarIcon(
            item = NavItem(
                painterResId = R.drawable.ic_more_vert,
                descriptionResId = R.string.action_more,
                onClick = { expanded = true },
            ),
        )
        MaterialTheme(colors = AppTheme.colors.copy(surface = Color.Transparent)) {
            DropdownMenu(
                modifier = Modifier.appTheme(
                    shape = AppCardShape,
                    tone = GlassTone.Strong,
                    elevation = 12.dp,
                ),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            item.onClick()
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            painter = painterResource(id = item.painterResId),
                            contentDescription = null,
                            tint = AppTheme.glass.accent,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(id = item.descriptionResId),
                            color = AppTheme.glass.content,
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBarIcon(item: NavItem) {
    TopBarIconWell {
        AppIconButton(
            painter = painterResource(id = item.painterResId),
            contentDescription = stringResource(id = item.descriptionResId),
            tint = AppTheme.glass.accent,
            onClick = item.onClick,
        )
    }
}

@Composable
private fun TopBarIconWell(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .appThemeControl(shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Preview
@Composable
private fun TopBarPreview(
    @PreviewParameter(TopBarPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        TopBar(
            title = "Title",
            backButton = {
                AppIconButton(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.action_back),
                    tint = AppTheme.glass.content,
                    onClick = {},
                )
            },
            items = emptyList(),
        )
    }
}


private class TopBarPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
