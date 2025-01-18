package com.example.constructionmanagement.composables.screens


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(isBottomSheetVisibleOverride: MutableState<Boolean>? = null) {
    val isBottomSheetVisible = isBottomSheetVisibleOverride ?: remember { mutableStateOf(false) }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }
    val selectedArea = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val selectedMediaUri = remember { mutableStateOf<Uri?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                    // Handle log submission
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
            PreviousLogs()
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LogsTopBar(onAddClick: () -> Unit) {
//    TopAppBar(
//        title = {
//            Box (
//                modifier = Modifier
//                    .padding(start = 10.dp, bottom = 18.dp)
//            ){
//                Text(
//                    text = "Log Entries",
//                    fontFamily = FontFamily.Monospace,
//                    fontWeight = FontWeight.SemiBold,
//                    fontSize = 35.sp,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier
//                        .padding(0.dp)
//                )
//            }
//             },
//        actions = {
//            FloatingActionButton(
//                onClick = onAddClick,
//                modifier = Modifier
//                    .size(50.dp)
//                    .padding(bottom = 10.dp, end = 5.dp)
//                    ) {
//                Icon(Icons.Default.Add, contentDescription = "Add Entry")
//            }
//        },
//        modifier = Modifier
//            .padding(15.dp)
//            .shadow(5.dp, shape = RoundedCornerShape(3.dp)),
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color(0xFFE3E6EF)
//        )
//    )
//}

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

    Column(modifier = Modifier.padding(16.dp)) {
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
            Text(
                text = "Selected Media: $uri",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            //code content to display captured media
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
fun PreviousLogs() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = "Logs for the last 3 days:",
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
            // add code that will hold a box for each log
        ) {
            Text(
                text = "No logs available.",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
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

@Preview(showBackground = true)
@Composable
fun PreviewLogList() {
    PreviousLogs()
}

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
