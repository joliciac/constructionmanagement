package com.example.constructionmanagement.composables.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.constructionmanagement.composables.screens.HomeScreen
import com.example.constructionmanagement.composables.screens.LogsScreen
import com.example.constructionmanagement.composables.screens.SettingsScreen
import com.example.constructionmanagement.composables.screens.SplashScreen
import com.example.constructionmanagement.composables.screens.WeatherScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = "splash", modifier = Modifier.padding(paddingValues)) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("home") {
            HomeScreen()
        }
        composable("logs"){
            LogsScreen(navController = navController)
        }
        composable("weather"){
            WeatherScreen()
        }
        composable("settings"){
            SettingsScreen()
        }
    }
}