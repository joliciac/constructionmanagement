package com.example.constructionmanagement

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.constructionmanagement.composables.navigation.BottomNavigationBar
import com.example.constructionmanagement.composables.navigation.NavigationGraph
import com.example.constructionmanagement.ui.theme.ConstructionManagementTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConstructionManagementTheme {
                val navController = rememberNavController()
                // Determine the current route
                val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = navController.currentBackStackEntry)

                Scaffold(
                    bottomBar = {
                        if (currentRoute.value?.destination?.route !in listOf("splash")) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { paddingValues ->
                    NavigationGraph(navController, paddingValues)
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