package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.constructionmanagement.R
import com.example.constructionmanagement.composables.data.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = WeatherViewModel()) {
    val weather by viewModel.weather

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(location = "40.7128,-74.0060") // Replace with dynamic location
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(
                painter = painterResource(id = R.drawable.partly_cloudy_day_24px),
                title = "Weather"
            )
            Spacer(modifier = Modifier.height(24.dp))

        if (weather == null) {
            Text(
                text = "Fetching weather...",
                style = MaterialTheme.typography.titleSmall
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreen()
}
