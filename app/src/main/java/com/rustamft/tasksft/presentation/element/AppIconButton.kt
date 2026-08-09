package com.rustamft.tasksft.presentation.element

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
internal fun AppIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Preview
@Composable
private fun AppIconButtonPreview(
    @PreviewParameter(AppIconButtonPreviewParameter::class) theme: Preferences.Theme,
) {
    AppTheme(theme = theme) {
        AppIconButton(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "",
            tint = AppTheme.glass.content,
            onClick = {},
        )
    }
}

private class AppIconButtonPreviewParameter : PreviewParameterProvider<Preferences.Theme> {
    override val values = sequenceOf(
        Preferences.Theme.Light,
        Preferences.Theme.Dark,
    )
}
