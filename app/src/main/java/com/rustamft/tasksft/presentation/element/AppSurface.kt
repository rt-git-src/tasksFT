package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.presentation.theme.AppCardShape

@Composable
internal fun AppSurface(
    modifier: Modifier = Modifier,
    shape: Shape = AppCardShape,
    tone: GlassTone = GlassTone.Regular,
    elevation: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .appTheme(
                shape = shape,
                tone = tone,
                elevation = elevation,
            )
            .padding(contentPadding),
        content = content,
    )
}
