package org.example
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            if (showNav) MementoMenuBar(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.primary
    )
    {innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MenuBarGraph(navController = navController)
        }
    }
}


@Composable
fun MementoMenuBar(navController: NavHostController) {
    val screens = listOf(
        MenuBarOptions.Settings,
        MenuBarOptions.Home,
        MenuBarOptions.Discover
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .padding(0.dp)
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
//        Surface(
//            shape = RoundedCornerShape(16.dp),
//            color = MaterialTheme.colorScheme.primary
//        ) {
//            NavigationBar(
//                containerColor = MaterialTheme.colorScheme.secondary,
//                contentColor = MaterialTheme.colorScheme.tertiary,
//                tonalElevation = 0.dp
//            ) {
//                screens.forEach { screen ->
//                    AddItem(
//                        screen = screen,
//                        currentDestination = currentDestination,
//                        navController = navController,
//                    )
//                }
//            }
//        }
        }
    }
}

@Composable
fun AddItem(
    screen: MenuBarOptions,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any {
        val test: String = it.route?: ""
        test.split("/")[0] == screen.route
    } == true

    Box(
        modifier = Modifier
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
            .padding(30.dp, 0.dp),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (selected) screen.icon_filled else screen.icon_outlined,
                contentDescription = "Navigation Icon",
                modifier = Modifier.size(32.dp),
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
            Text(
                text = screen.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
        }
    }

//
//    NavigationBarItem(
//        label = {
//            Text(
//                text = screen.title,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold,
//                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
//            )
//        },
//        icon = {
//            Surface(
//                shape =  RoundedCornerShape(16.dp),
//                color = Color.Transparent
//            ) {
//                Icon(
//                    imageVector = if (selected) screen.icon_filled else screen.icon_outlined,
//                    contentDescription = "Navigation Icon",
//                    modifier = Modifier.size(32.dp),
//                    tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
//                )
//            }
//               },
//        modifier = Modifier.fillMaxSize()
//            .then(indicatorModifier),
//        selected = selected,
//        onClick = {
//            navController.navigate(screen.route) {
//                popUpTo(navController.graph.findStartDestination().id)
//                launchSingleTop = true
//            }
//        }
//    )
}
