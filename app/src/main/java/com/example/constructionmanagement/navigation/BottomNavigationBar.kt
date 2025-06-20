package com.example.constructionmanagement.navigation

import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Logs,
        BottomNavItem.Weather,
        BottomNavItem.Settings
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 9.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    if (isSelected) {
                        Icon(item.filledIcon, contentDescription = item.title)
                    } else {
                        Icon(item.outlinedIcon, contentDescription = item.title, tint = MaterialTheme.colorScheme.scrim)
                    }
                },
                label = { Text(item.title, color = MaterialTheme.colorScheme.scrim) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}