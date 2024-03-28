package org.example.userinterface

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuBarOptions (
    val route: String,
    val title: String,
    val icon_filled: ImageVector,
    val icon_outlined: ImageVector
) {
    data object Login: MenuBarOptions(
        route = "login",
        title = "Login",
        icon_filled = Icons.Default.ExitToApp,
        icon_outlined = Icons.Default.ExitToApp
    )

    data object Home: MenuBarOptions(
        route = "home",
        title = "Home",
        icon_filled = Icons.Default.Home,
        icon_outlined = Icons.Outlined.Home
    )

    data object HomeInfo: MenuBarOptions(
        route = "home/info",
        title = "Home Info",
        icon_filled = Icons.Default.ExitToApp,
        icon_outlined = Icons.Default.ExitToApp
    )

    data object Discover: MenuBarOptions(
        route = "discover",
        title = "Discover",
        icon_filled = Icons.Default.Person,
        icon_outlined = Icons.Outlined.Person
    )

    data object History: MenuBarOptions(
        route = "history",
        title = "History",
        icon_filled = Icons.Default.Person,
        icon_outlined = Icons.Outlined.Person
    )

    data object Settings: MenuBarOptions(
        route = "settings",
        title = "Settings",
        icon_filled = Icons.Default.Settings,
        icon_outlined = Icons.Outlined.Settings
    )
}