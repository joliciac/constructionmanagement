package com.example.constructionmanagement.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.constructionmanagement.composables.screens.HomeScreen
import com.example.constructionmanagement.composables.screens.LoginScreen
import com.example.constructionmanagement.composables.screens.LogsScreen
import com.example.constructionmanagement.composables.screens.SettingsScreen
import com.example.constructionmanagement.composables.screens.SignupScreen
import com.example.constructionmanagement.composables.screens.SplashScreen
import com.example.constructionmanagement.composables.screens.WeatherScreen
import com.example.constructionmanagement.viewmodel.SettingsScreenViewModel
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean
) {
    val settingsViewModel: SettingsScreenViewModel = viewModel()

    val onDeleteAccountClick = {
        settingsViewModel.deleteUserAccount(
            onSuccess = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("settings") { inclusive = true }
                }
            },
            onFailure = { error ->
                Log.e("NavigationGraph", "Account deletion failed: ${error?.message}")
            }
        )
    }

    NavHost(navController, startDestination = "splash", modifier = Modifier.padding(paddingValues)) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "admin") {
                        navController.navigate("home")
                    } else {
                        navController.navigate("logs")
                        }
                    },
                onNavigateToSignup = {
                    navController.navigate("signup") }
            )
        }
        composable("signup") {
            SignupScreen(
                onSignupSuccess = {navController.navigate("login")},
                onNavigateToLogin = {navController.navigate("login")}
                )
        }
        composable("home") {
            HomeScreen()
        }
        composable("logs"){
            LogsScreen()
        }
        composable("weather"){
            WeatherScreen()
        }
        composable("settings"){
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeClick = onThemeToggle,
                onNotificationClick = {
                    println("Notification settings clicked")
                },
                onLanguageChange = {
                    // Action to change language
                    println("Language change clicked")
                },
                onLogoutClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onDeleteAccountClick = onDeleteAccountClick
            )
        }
    }
}