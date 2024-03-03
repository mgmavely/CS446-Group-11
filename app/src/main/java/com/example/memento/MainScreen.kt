package org.example
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color

import com.example.memento.R
import org.example.userinterface.MenuBarGraph
import org.example.userinterface.MenuBarOptions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun MementoApp(
    navController: NavHostController = rememberNavController()
) {
    var showNav by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    showNav = when (navBackStackEntry?.destination?.route) {
        MenuBarOptions.Login.route -> false // on this screen bottom bar should be hidden
        else -> true // in all other cases show bottom bar
    }
    Scaffold(
        bottomBar = {
            if (showNav) WaitlessMenuBar(navController = navController)
        }
    )
    {
        MenuBarGraph(navController = navController)
    }
}


@Composable
fun WaitlessMenuBar(navController: NavHostController) {
    val screens = listOf(
        MenuBarOptions.Home,
        MenuBarOptions.Discover,
        MenuBarOptions.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: MenuBarOptions,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(
                text = screen.title,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Black
            )
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
                modifier = Modifier.fillMaxSize(0.35f),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.fillMaxSize(),
        selected = currentDestination?.hierarchy?.any {
            val test: String = it.route?: ""
            test.split("/")[0] == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}
