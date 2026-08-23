package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.theme.AppControlShape
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.appPressable

@Composable
internal fun AppValueControl(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .heightIn(min = 48.dp)
            .appPressable(
                shape = AppControlShape,
                onClick = onClick,
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = AppTheme.glass.content,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.ic_chevron_down),
            contentDescription = null,
            tint = AppTheme.glass.contentMuted,
        )
    }
}
