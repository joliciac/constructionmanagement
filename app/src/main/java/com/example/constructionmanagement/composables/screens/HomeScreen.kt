package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.R
import com.example.constructionmanagement.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userRole: String, viewModel: HomeScreenViewModel = viewModel()) {
    val isAdmin = userRole == "Admin"
    val progress by viewModel.progress.collectAsState()

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
            Spacer(modifier = Modifier.padding(30.dp))

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
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                    Spacer(modifier = Modifier.height(16.dp))
                Text("Viewing Progress")
            }
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
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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



@Preview(showBackground = true)
@Composable
fun HomeScreenScaffoldPreview(){
    HomeScreen(userRole = "Admin")
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