package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.R
import com.example.constructionmanagement.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {
    val userRole by viewModel.userRole.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val isAdmin = userRole == "Admin"

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(
                title = "Home Feed",
                painter = painterResource(id = R.drawable.home_work_24px)
            )
            Spacer(modifier = Modifier.height(10.dp))

            ProgressBar(
                progress = progress,
                isAdmin = isAdmin,
                onProgressChange = { newProgress ->
                    if (isAdmin) {
                        viewModel.updateProgress(newProgress)
                    }
                }
            )
            if (isAdmin) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Edit Progress")
                Slider(
                    value = progress,
                    onValueChange = { newProgress ->
                        viewModel.updateProgress(newProgress)
                    },
                    valueRange = 0f..1f,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF351D43),
                        activeTrackColor = Color(0xFF351D43),
                        inactiveTrackColor = Color(0xFFF3E9F9)
                    )
                )
            }
            CheckInAndOut(viewModel = viewModel)
            TaskUpdates()
        }
    }
}

@Composable
fun ProgressBar(
    milestones: Int = 5,
    progress: Float = 0.5f,
    isAdmin: Boolean,
    onProgressChange: (Float) -> Unit = {} // Callback for admin updates
) {
    val milestoneSpacing = (1f / (milestones - 1))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Project Milestone")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until milestones) {
                val isCompleted = (i * milestoneSpacing) <= progress

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(if (isCompleted) Color.DarkGray else Color.White, shape = CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )

                if (i < milestones - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
fun CheckInAndOut(viewModel: HomeScreenViewModel = viewModel()) {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val checkInTime by viewModel.checkInTime.collectAsState()
    val isCheckedIn = checkInTime != null

    val hours = (elapsedTime / 3600).toInt()
    val minutes = ((elapsedTime % 3600) / 60).toInt()
    val seconds = (elapsedTime % 60).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F0F9),
            contentColor = Color(0xFF351D43)
        )
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
            if (!isCheckedIn) {
                RadioButton(
                    selected = false,
                    onClick = { viewModel.checkIn() },
                    modifier = Modifier.padding(16.dp),
                    enabled = true
                )
                Text("Check-In")
            }

            Text(
                text = "%02d:%02d:%02d".format(hours, minutes, seconds),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (isCheckedIn) {
                RadioButton(
                    selected = true,
                    onClick = { viewModel.checkOut() },
                    modifier = Modifier.padding(16.dp),
                    enabled = true
                )
                Text("Check-Out")
            }
        }
    }
}

@Composable
fun TaskUpdates() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F0F9),
            contentColor = Color(0xFF351D43)
        )
    ){
        Text("Today's Task Summary: ",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenScaffoldPreview(){
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewMilestoneProgressBar() {
    ProgressBar(
        progress = 0.5f,
        milestones = 3,
        isAdmin = false,
        onProgressChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CheckInAndOutPreview(){
    CheckInAndOut()
}