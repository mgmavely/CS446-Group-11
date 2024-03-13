package org.example.userinterface

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.example.userinterface.Equipment.DiscoverView
import org.example.userinterface.Equipment.EquipmentInfo.HomeInfoView
import org.example.userinterface.Home.HomeView
import org.example.userinterface.Login.LoginView
import org.example.userinterface.Settings.SettingsView

@Composable
fun MenuBarGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MenuBarOptions.Login.route,
    ) {
        composable(route = MenuBarOptions.Login.route) {
            LoginView(toHomePage = { navController.navigate(MenuBarOptions.Home.route) })
        }
        composable(route = MenuBarOptions.Home.route) {
            HomeView(onHomeClicked = { navController.navigate(MenuBarOptions.HomeInfo.route) })
        }
        composable(route = MenuBarOptions.HomeInfo.route) {
            HomeInfoView()
        }
        composable(route = MenuBarOptions.Settings.route) {
            SettingsView( onLogoutClicked = { navController.navigate(MenuBarOptions.Login.route) } )
        }
        composable(route = MenuBarOptions.Discover.route) {
            DiscoverView()
        }
    }
}