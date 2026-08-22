package com.rustamft.tasksft.presentation.navigation

internal data class NavItem(
    val painterResId: Int,
    val descriptionResId: Int,
    val enabled: Boolean = true,
    val onClick: () -> Unit = {},
)
