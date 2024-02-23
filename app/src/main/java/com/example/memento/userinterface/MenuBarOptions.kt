package org.example.userinterface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuBarOptions (
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Login: MenuBarOptions(
        route = "login",
        title = "Login",
        icon = Icons.Default.ExitToApp
    )

    data object Home: MenuBarOptions(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object HomeInfo: MenuBarOptions(
        route = "home/info",
        title = "Home Info",
        icon = Icons.Default.ExitToApp
    )

    data object Discover: MenuBarOptions(
        route = "discover",
        title = "Discover",
        icon = Icons.Default.KeyboardArrowUp
    )

    data object Settings: MenuBarOptions(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}

