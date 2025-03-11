package com.example.constructionmanagement.composables.screens


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.constructionmanagement.data.LogEntry
import com.example.constructionmanagement.viewmodel.LogsScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(isBottomSheetVisibleOverride: MutableState<Boolean>? = null, viewModel: LogsScreenViewModel = viewModel()) {
    val isBottomSheetVisible = isBottomSheetVisibleOverride ?: remember { mutableStateOf(false) }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val selectedArea = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val selectedMediaUri = remember { mutableStateOf<Uri?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val logs by rememberUpdatedState(viewModel.logs)


    if (isBottomSheetVisible.value) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible.value = false },
            sheetState = sheetState
        ) {
            LogEntryBottomSheet(
                date = date,
                time = time,
                selectedArea = selectedArea,
                description = description,
                selectedMediaUri = selectedMediaUri,
                onDismiss = { isBottomSheetVisible.value = false },
                onSubmit = {
                    val logEntry = LogEntry(
                        date = date.value,
                        time = time.value,
                        area = selectedArea.value,
                        description = description.value,
                        mediaUri = selectedMediaUri.value?.toString()
                    )
                    viewModel.submitLog(logEntry){ success ->
                        if (success) {
                            Toast.makeText(context, "Log submitted!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Submission failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    date.value = ""
                    time.value = ""
                    selectedArea.value = ""
                    description.value = ""
                    selectedMediaUri.value = null

                    isBottomSheetVisible.value = false
                }
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            BottomLogFloatingActionButton(onAddClick = { isBottomSheetVisible.value = true }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(
                icon = Icons.Default.Build, title = "Log Entries",onIconClick = { isBottomSheetVisible.value = true })
            Spacer(modifier = Modifier.height(16.dp) )
            PreviousLogs(logs = logs)
        }
    }
}


@Composable
fun BottomLogFloatingActionButton(onAddClick: () -> Unit) {
    FloatingActionButton(onClick = onAddClick) {
        Icon(Icons.Default.Add, contentDescription = "Add Log Entry")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Bottom Sheet Code
@Composable
fun LogEntryBottomSheet(
    date: MutableState<String>,
    time: MutableState<String>,
    selectedArea: MutableState<String>,
    description: MutableState<String>,
    selectedMediaUri: MutableState<Uri?>,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    // Dropdown state
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val siteAreas = listOf("North Wing", "South Wing", "East Wing", "West Wing")

    val context = LocalContext.current

    val pickMediaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedMediaUri.value = it
            Toast.makeText(context, "Media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Add a New Log Entry",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextButton(
            onClick = { showDatePicker.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFF9E3D3),
                containerColor = Color(0xFFE3E2E6),
                contentColor = Color(0xFF445E91)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Select Date: ${date.value.ifEmpty { "dd-MM-yyyy" }}")
        }

        if (showDatePicker.value) {
            DatePickerModalInput(
                onDateSelected = { selectedDateMillis ->
                    selectedDateMillis?.let {
                        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
                        date.value = formattedDate
                    }
                },
                onDismiss = { showDatePicker.value = false }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = time.value,
            onValueChange = { time.value = it },
            label = { Text("Time") },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF9E3D3)
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select Site Area:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { isDropdownExpanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp
                ),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE3E2E6),
                    contentColor = Color(0xFF445E91)
                )
            ) {
                Text(selectedArea.value.ifEmpty { "Select Site Area" })
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "DropDownArrow")
            }
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                siteAreas.forEach { area ->
                    DropdownMenuItem(
                        onClick = {
                            selectedArea.value = area
                            isDropdownExpanded = false
                        },
                        text = {
                            Text(text = area)
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { pickMediaLauncher.launch("image/* video/*") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8A9ECB)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            Text("Media Upload")
            Icon(
               imageVector = Icons.Default.AddCircle,
                contentDescription = "MediaPlusSign",
                modifier = Modifier
                .padding(start = 5.dp))
        }
        selectedMediaUri.value?.let { uri ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Selected Media:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(bottom = 10.dp)
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (uri.toString().contains("image")) {
                    // Display Image
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else if (uri.toString().contains("video")) {
                    // Placeholder for Video
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Video Icon",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(text = "Unsupported media type", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE3E6EF),
                    contentColor = Color(0xFF445E91)
                )
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = onSubmit,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE3E6EF),
                    contentColor = Color(0xFF445E91)
                )
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun PreviousLogs(logs: List<LogEntry>) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = "Logs for the last 3 days:",
            style = MaterialTheme.typography.titleLarge,
//            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFFE3E2E6),
            border = BorderStroke(width = 1.dp, color = Color.DarkGray)
            // add code that will hold a box for each log
        ) {
            if (logs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No logs available.",
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    items(logs) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Date: ${log.date}", style = MaterialTheme.typography.bodyMedium)
                                Text("Time: ${log.time}", style = MaterialTheme.typography.bodyMedium)
                                Text("Area: ${log.area}", style = MaterialTheme.typography.bodyMedium)
                                Text("Description: ${log.description}", style = MaterialTheme.typography.bodyMedium)
                                log.mediaUri?.let {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Media: $it", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun LogsScreenPreview(){
//    val previewNavController = rememberNavController()
//    val isBottomSheetVisible = remember { mutableStateOf(true) }
//
//    LogsScreen(navController = previewNavController, isBottomSheetVisibleOverride = isBottomSheetVisible)
//}

@Preview(showBackground = true)
@Composable
fun PreviewLogsTopBar() {
    ScreenHeader(onIconClick = {}, icon = Icons.Default.Build, title = "Log Entries")
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLogList() {
//    PreviousLogs(
//        logs =
//    )
//}

@Preview(showBackground = true, name = "Log Entry Bottom Sheet Preview")
@Composable
fun LogEntryBottomSheetPreview() {
    val date = remember { mutableStateOf("16/01/2025") }
    val time = remember { mutableStateOf("10:00 AM") }
    val selectedArea = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("Work completed on the site.") }
    val selectedMediaUri = remember { mutableStateOf<Uri?>(null) }

    LogEntryBottomSheet(
        date = date,
        time = time,
        selectedArea = selectedArea,
        description = description,
        selectedMediaUri = selectedMediaUri,
        onDismiss = { },
        onSubmit = { }
    )
}
