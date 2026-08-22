package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.element.AppSurface
import com.rustamft.tasksft.presentation.preview.ThemePreviewProvider
import com.rustamft.tasksft.presentation.theme.AppBarShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.GlassTone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(
    title: String,
    leadingItem: NavItem,
    items: List<NavItem> = emptyList(),
    dropdownItems: List<NavItem> = emptyList(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        AppSurface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(min = 224.dp)
                .windowInsetsPadding(WindowInsets.statusBars),
            shape = AppBarShape,
            tone = GlassTone.Strong,
            elevation = 12.dp,
            contentPadding = PaddingValues(0.dp),
        ) {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 4.dp),
                windowInsets = WindowInsets(),
                title = {
                    Text(
                        text = title,
                        color = AppTheme.glass.content,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    TopBarIcon(leadingItem)
                },
                actions = {
                    items.forEach { item ->
                        Spacer(modifier = Modifier.width(8.dp))
                        TopBarIcon(item)
                    }
                    if (dropdownItems.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TopBarDropdown(items = dropdownItems)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        }
    }
}

@Preview
@Composable
private fun TopBarPreview(
    @PreviewParameter(ThemePreviewProvider::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        TopBar(
            title = "Title",
            leadingItem = NavItem(
                painterResId = R.drawable.ic_arrow_back,
                descriptionResId = R.string.action_back,
            ),
            items = listOf(
                NavItem(
                    painterResId = R.drawable.ic_settings,
                    descriptionResId = R.string.action_settings,
                )
            ),
        )
    }
}
