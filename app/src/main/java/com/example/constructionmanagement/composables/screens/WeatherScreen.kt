package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.constructionmanagement.composables.data.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = WeatherViewModel()) {
    val weather by viewModel.weather

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(location = "40.7128,-74.0060") // Replace with dynamic location
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (weather == null) {
            Text(text = "Fetching weather...", style = MaterialTheme.typography.titleSmall)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Temperature: ${weather?.temperature ?: "N/A"}°C",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Condition: ${weather?.description ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}
//fun WeatherScreen() {

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = "Weather Screen", style = MaterialTheme.typography.titleSmall)
//    }
//}