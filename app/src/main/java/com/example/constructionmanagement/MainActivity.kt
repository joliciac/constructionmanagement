package com.example.constructionmanagement

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.constructionmanagement.navigation.BottomNavigationBar
import com.example.constructionmanagement.navigation.NavigationGraph
import com.example.constructionmanagement.ui.theme.ConstructionManagementTheme
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            FirebaseDatabase.getInstance()
                .setPersistenceEnabled(true)
        } catch (e: DatabaseException) {
            Log.w("MainActivity", "Firebase persistence already enabled or late initialization: ${e.message}")
        }

        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            ConstructionManagementTheme(
                darkTheme = isDarkTheme
            ) {
                val navController = rememberNavController()
                val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

                Scaffold(
                    bottomBar = {
                        if (currentRoute.value?.destination?.route !in listOf("splash", "login", "signup")) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { paddingValues ->
                    NavigationGraph(
                        navController = navController,
                        paddingValues = paddingValues,
                        onThemeToggle = { isDarkTheme = !isDarkTheme},
                        isDarkTheme = isDarkTheme)
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ConstructionManagementTheme {
//        Greeting("Android")
//    }
//}