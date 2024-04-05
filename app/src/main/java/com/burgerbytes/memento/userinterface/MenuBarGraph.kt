package org.burgerbytes.userinterface

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.burgerbytes.memento.mvvm.viewmodel.HomeViewModel
import org.burgerbytes.userinterface.Equipment.DiscoverView
import org.burgerbytes.userinterface.History.HistoryView
import org.burgerbytes.userinterface.Home.HomeView
import org.burgerbytes.userinterface.Login.LoginView
import org.burgerbytes.userinterface.Settings.SettingsView

@Composable
fun MenuBarGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
) {
        NavHost(
            navController = navController,
            startDestination = MenuBarOptions.Login.route,
        ) {
            composable(route = MenuBarOptions.Login.route) {
                LoginView(toHomePage = { navController.navigate(MenuBarOptions.Home.route) },                 isDarkMode = isDarkMode,)
            }
            composable(route = MenuBarOptions.Home.route) {
                HomeView(
                    onHomeClicked = { navController.navigate(MenuBarOptions.HomeInfo.route) },
                    toHistory = { navController.navigate(MenuBarOptions.History.route) },
                    viewModel = HomeViewModel(),
                    isDarkMode = isDarkMode
                )
            }
            composable(route = MenuBarOptions.Settings.route) {
                SettingsView(
                    onLogoutClicked = { navController.navigate(MenuBarOptions.Login.route) },
                    isDarkMode = isDarkMode,
                    toggleDarkMode = toggleDarkMode,
                )
            }
            composable(route = MenuBarOptions.Discover.route) {
                DiscoverView(isDarkMode = isDarkMode)
            }
            composable(route = MenuBarOptions.History.route) {
                HistoryView(isDarkMode = isDarkMode)
            }
        }
}
