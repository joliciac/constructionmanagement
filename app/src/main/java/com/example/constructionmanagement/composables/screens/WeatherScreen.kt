package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.constructionmanagement.R
import com.example.constructionmanagement.data.DailyWeather
import com.example.constructionmanagement.data.HourlyWeather
import com.example.constructionmanagement.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherResponse by viewModel.weather

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(latitude = 51.5072, longitude = 0.1276) // London coordinates
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

            if (weatherResponse == null) {
                Text("Fetching weather...")
            } else {
                if (weatherResponse?.hourly.isNullOrEmpty()) {
                    Text("No hourly data available.")
                } else {
                    // Displaying Hourly Weather as a LazyRow
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        items(weatherResponse?.hourly ?: emptyList()) { hourlyWeather ->
                            WeatherHourly(hourlyWeather = hourlyWeather)
                        }
                    }
                }

                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(weatherResponse?.daily ?: emptyList()) { dailyWeather ->
                                WeatherForecast(dailyWeather = dailyWeather)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherForecast(dailyWeather: DailyWeather) {
    val weatherIcon = "https://openweathermap.org/img/wn/${dailyWeather.weather[0].icon}.png"
    val date = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date(dailyWeather.dt * 1000))
    val isRainy = dailyWeather.weather.any { it.description.contains("rain", ignoreCase = true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp),
        colors = CardDefaults.cardColors(if (isRainy) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Temperature: ${dailyWeather.temp.day}°C")
                Text("Condition: ${dailyWeather.weather[0].description}")
            }
            Image(painter = rememberAsyncImagePainter(weatherIcon), contentDescription = "Weather Icon")
        }
    }
}

@Composable
fun WeatherHourly(hourlyWeather: HourlyWeather) {
    val weatherIcon = "https://openweathermap.org/img/wn/${hourlyWeather.weather[0].icon}.png"
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(hourlyWeather.dt * 1000))

    Card(
        modifier = Modifier
            .width(110.dp)
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.233f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Text("${hourlyWeather.temp}°C")
            Text("${hourlyWeather.weather[0].description}")
            Image(painter = rememberAsyncImagePainter(weatherIcon), contentDescription = "Weather Icon")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreen(viewModel = WeatherViewModel())
}
