package com.example.constructionmanagement.composables.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.constructionmanagement.data.logs.LogEntry
import com.example.constructionmanagement.viewmodel.LogsScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen( viewModel: LogsScreenViewModel = viewModel()) {
    val isBottomSheetVisible = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val logs by rememberUpdatedState(viewModel.logs)
    val selectedLog by viewModel.selectedLog.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val isAdmin = userRole == "Admin"

    val filteredLogs = if (isAdmin) {
        logs
    } else {
        logs.filter{ it.userId == viewModel.auth.currentUser?.uid}
    }
    if (isBottomSheetVisible.value) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible.value = false },
            sheetState = sheetState
        ) {
            LogEntryBottomSheet(
                selectedLog = selectedLog,
                onDismiss = { isBottomSheetVisible.value = false },
                onSubmit = { updatedLog ->
                    if (selectedLog != null) {
                        viewModel.updateLog(updatedLog) { success ->
                            if (success) {
                                Toast.makeText(context, "Log updated!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        viewModel.submitLog(updatedLog) { success ->
                            if (success) {
                                Toast.makeText(context, "Log submitted!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Submission failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    isBottomSheetVisible.value = false
                }
            )
        }
    }
    selectedLog?.let { log ->
        AlertDialog(
            onDismissRequest = { viewModel.hideLogOptions() },
            title = { Text("Edit or Delete Log") },
            text = { Text("Do you want to edit or delete this log?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.setSelectedLog(log)
                    isBottomSheetVisible.value = true
                }) {
                    Text("Edit")
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.deleteLog(log) { success ->
                        if (success) {
                            Toast.makeText(context, "Log deleted!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to delete log", Toast.LENGTH_SHORT).show()
                        }
                    }
                    viewModel.hideLogOptions()
                }) {
                    Text("Delete")
                }
            }
        )
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
            PreviousLogs(
                logs = filteredLogs,
                onLogClick = {viewModel.showLogOptions(it)}
            )
        }
    }
}

@Composable
fun BottomLogFloatingActionButton(onAddClick: () -> Unit) {
    FloatingActionButton(onClick = onAddClick, containerColor = MaterialTheme.colorScheme.secondary) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add Log Entry")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = 0, // Default hour
        initialMinute = 0, // Default minute
        is24Hour = true
    )

    // Displaying the TimePicker in a dialog
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 3.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                TimePicker(state = timePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    TextButton(onClick = {
                        val formattedTime = String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onTimeSelected(formattedTime)
                        onDismiss() // Dismiss the dialog
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


// Bottom Sheet Code
@Composable
fun LogEntryBottomSheet(
    selectedLog: LogEntry?,
    onDismiss: () -> Unit,
    onSubmit: (LogEntry) -> Unit
) {
    // I moved this from log screen composable so that the bottom sheet can manage its own state
    val title = remember { mutableStateOf( "") }
    val date = remember { mutableStateOf( "") }
    val time = remember { mutableStateOf( "") }
    val selectedArea = remember { mutableStateOf( "") }
    val description = remember { mutableStateOf("") }
    val selectedMediaUri = remember { mutableStateOf<Uri?>(null) }

    val showDatePicker = remember { mutableStateOf(false) }
    val showTimePicker = remember { mutableStateOf(false) }
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

    LaunchedEffect(selectedLog) {
        selectedLog?.let {
            title.value = it.title
            date.value = it.date
            time.value = it.time
            selectedArea.value = it.area
            description.value = it.description
            selectedMediaUri.value = it.mediaUri?.let { uriString -> Uri.parse(uriString) }
        }
    }

    if (showTimePicker.value) {
        TimePickerDialog(
            onTimeSelected = { selectedTime ->
                time.value = selectedTime
            },
            onDismiss = { showTimePicker.value = false }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (selectedLog != null) "Edit Log Entry " else "Add a New Log Entry",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = title.value,
            onValueChange = { title.value = it},
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = { showDatePicker.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.surfaceVariant
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

        TextButton(
            onClick = { showTimePicker.value = true },
            modifier = Modifier
                .fillMaxSize(),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = "Select Time: ${time.value.ifEmpty { "Not Set" }}")
        }

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
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.surfaceVariant
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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { pickMediaLauncher.launch("image/* video/*") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                contentColor = MaterialTheme.colorScheme.surfaceVariant
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
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    contentColor = MaterialTheme.colorScheme.surfaceTint
                )
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    val updatedLog = selectedLog?.copy(
                        title = if (title.value.isNotBlank()) title.value else selectedLog.title,
                        date = if (date.value.isNotBlank()) date.value else selectedLog.date,
                        time = if (time.value.isNotBlank()) time.value else selectedLog.time,
                        area = if (selectedArea.value.isNotBlank()) selectedArea.value else selectedLog.area,
                        description = if (description.value.isNotBlank()) description.value else selectedLog.description,
                        mediaUri = selectedMediaUri.value?.toString() ?: selectedLog.mediaUri
                    ) ?: LogEntry(
                        logId = UUID.randomUUID().toString(),
                        title = title.value,
                        date = date.value,
                        time = time.value,
                        area = selectedArea.value,
                        description = description.value,
                        mediaUri = selectedMediaUri.value?.toString()
                    )
                    onSubmit(updatedLog)
                },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    contentColor = MaterialTheme.colorScheme.surfaceTint
                )
            ) {
                Text(if (selectedLog != null) "Update Log" else "Submit")
            }
        }
    }
}

@Composable
fun PreviousLogs(logs: List<LogEntry>, onLogClick: (LogEntry) -> Unit) {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = "Previous Log Entries:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.tertiary,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
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
                                .padding(vertical = 6.dp)
                                .clickable { onLogClick(log) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.surface),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Title: ${log.title}", style = MaterialTheme.typography.bodyMedium)
                                Text("Date: ${log.date}", style = MaterialTheme.typography.bodyMedium)
                                Text("Area: ${log.area}", style = MaterialTheme.typography.bodyMedium)
                                Text("Description: ${log.description}", style = MaterialTheme.typography.bodyMedium)
                                log.mediaUri?.let { mediaUri ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Media:", style = MaterialTheme.typography.bodyMedium)
                                    if (mediaUri.contains("image")) {
                                        Image(
                                            painter = rememberAsyncImagePainter(mediaUri),
                                            contentDescription = "Selected Image",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .padding(top = 8.dp),
                                            contentScale = ContentScale.Crop
                                        )
//                                    } else if (mediaUri.contains("video")) {
//                                        Icon(
//                                            imageVector = Icons.Default.AccountBox,
//                                            contentDescription = "Video Icon",
//                                            modifier = Modifier
//                                                .size(48.dp)
//                                                .padding(top = 8.dp),
//                                            tint = MaterialTheme.colorScheme.primary
//                                        )
//                                    }
                                    } else {
                                        Text(
                                            "Unsupported media type",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewLogsTopBar() {
//    ScreenHeader(onIconClick = {}, icon = Icons.Default.Build, title = "Log Entries")
//}
//
//
//@Preview(showBackground = true, name = "Log Entry Bottom Sheet Preview")
//@Composable
//fun LogEntryBottomSheetPreview() {
//    val title = remember { mutableStateOf("Cementing") }
//    val date = remember { mutableStateOf("16/01/2025") }
//    val time = remember { mutableStateOf("10:00 AM") }
//    val selectedArea = remember { mutableStateOf("") }
//    val description = remember { mutableStateOf("Work completed on the site.") }
//    val selectedMediaUri = remember { mutableStateOf<Uri?>(null) }
//
//    LogEntryBottomSheet(
//        onDismiss = { },
//        onSubmit = { },
//        selectedLog = TODO()
//    )
//}
