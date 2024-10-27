package com.example.constructionmanagement.composables.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(
        "home",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "Home"
    )
    object Logs : BottomNavItem(
        "logs",
        Icons.Filled.Build,
        Icons.Outlined.Build,
        "Logs")
    object Weather : BottomNavItem(
        "weather",
        Icons.Filled.Place,
        Icons.Outlined.Place,
        "Weather")
    object Settings : BottomNavItem(
        "settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        "Settings")
}