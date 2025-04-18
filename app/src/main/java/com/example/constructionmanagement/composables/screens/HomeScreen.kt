package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.R
import com.example.constructionmanagement.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {
    val userRole by viewModel.userRole.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val isAdmin = userRole == "Admin"

    LaunchedEffect(Unit) {
        viewModel.fetchTask()
        viewModel.fetchCheckInAndOutTime()
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
                title = "Home Feed",
                painter = painterResource(id = R.drawable.home_work_24px)
            )
            Spacer(modifier = Modifier.height(10.dp))
}
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
        ) {
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
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            CheckInAndOut(viewModel = viewModel)
            TaskUpdates()
            Spacer(modifier = Modifier.height(20.dp))
            MotivationalQuote()
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 150.dp),
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
                        .background(if (isCompleted) MaterialTheme.colorScheme.surfaceTint else Color.White, shape = CircleShape)
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
    val userRole by viewModel.userRole.collectAsState()
    val isWorker = userRole == "Worker"
    val isCheckedIn = checkInTime != null

    val hours = (elapsedTime / 3600).toInt()
    val minutes = ((elapsedTime % 3600) / 60).toInt()
    val seconds = (elapsedTime % 60).toInt()

    if (isWorker) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .size(55.dp),
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
}

@Composable
fun TaskUpdates(viewModel: HomeScreenViewModel = viewModel()) {
    val userRole by viewModel.userRole.collectAsState()
    val isAdmin = userRole == "Admin"
    val tasks by viewModel.tasks.collectAsState()
    val newTask = remember { mutableStateOf("") }

    val bungee = FontFamily(Font(R.font.bungee_shade))

    Card(
        modifier = Modifier
            .fillMaxHeight(0.73f)
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
            "Today's Task Summary: ",
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = bungee,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            if (isAdmin) {
                TextField(
                    value = newTask.value,
                    onValueChange = { newTask.value = it },
                    label = { Text("Enter a task")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.onTertiary
                    )
                )
                Button(
                    onClick = {
                        if (newTask.value.isNotEmpty()) {
                            viewModel.addTask(newTask.value)
                            newTask.value = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Add")
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {
                items(tasks) { task ->
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "• $task",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .wrapContentHeight()
                        )
                        if (isAdmin) {
                            IconButton(
                                onClick = { viewModel.deleteTask(task) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Task",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MotivationalQuote() {
    val shipporiRegular = FontFamily(Font(R.font.shippori_mincho_regular))

    Card(
        modifier = Modifier
            .fillMaxHeight(0.78f)
            .fillMaxWidth()
            .padding(2.dp),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(defaultElevation = 25.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Remember: Rome wasn't built in a day \n ⏳ ",
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenScaffoldPreview(){
    val viewModel = remember { HomeScreenViewModel() }
    HomeScreen(viewModel = viewModel())
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
