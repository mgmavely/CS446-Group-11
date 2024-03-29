package org.example.userinterface

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memento.mvvm.viewmodel.HomeViewModel
import org.example.userinterface.Equipment.DiscoverView
import org.example.userinterface.History.HistoryView
import org.example.userinterface.Equipment.EquipmentInfo.HomeInfoView
import org.example.userinterface.Home.HomeView
import org.example.userinterface.Login.LoginView
import org.example.userinterface.Settings.SettingsView

@Composable
fun MenuBarGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit
) {
        NavHost(
            navController = navController,
            startDestination = MenuBarOptions.Login.route,
        ) {
            composable(route = MenuBarOptions.Login.route) {
                LoginView(toHomePage = { navController.navigate(MenuBarOptions.Home.route) })
            }
            composable(route = MenuBarOptions.Home.route) {
                HomeView(
                    onHomeClicked = { navController.navigate(MenuBarOptions.HomeInfo.route) },
                    toHistory = { navController.navigate(MenuBarOptions.History.route) },
                    viewModel = HomeViewModel()
                )
            }
            composable(route = MenuBarOptions.HomeInfo.route) {
                HomeInfoView()
            }
            composable(route = MenuBarOptions.Settings.route) {
                SettingsView(
                    onMentalHealthOnlineClicked = {},
                    onMentalHealthPhoneClicked = {},
                    onLogoutClicked = { navController.navigate(MenuBarOptions.Login.route) },
                    isDarkMode = isDarkMode,
                    toggleDarkMode = toggleDarkMode
                )
            }
            composable(route = MenuBarOptions.Discover.route) {
                DiscoverView()
            }
            composable(route = MenuBarOptions.History.route) {
                HistoryView()
            }
        }
}
