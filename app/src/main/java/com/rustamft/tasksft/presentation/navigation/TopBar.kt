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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.element.AppIconButton
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.element.appThemeControl
import com.rustamft.tasksft.presentation.theme.AppBarShape
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
fun TopBar(
    title: String,
    backButton: (@Composable () -> Unit)? = null,
    leadingItem: NavItem? = null,
    items: List<NavItem>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        AppSurface(
            modifier = Modifier.fillMaxWidth(),
            shape = AppBarShape,
            contentPadding = PaddingValues(6.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when {
                    backButton != null -> TopBarIconWell(content = backButton)
                    leadingItem != null -> TopBarIcon(item = leadingItem)
                }

                if (backButton != null || leadingItem != null) {
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
            .size(44.dp)
            .appThemeControl(shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
