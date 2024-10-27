package com.example.constructionmanagement.composables.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(navController: NavController) {
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    Scaffold (
            topBar = {
                TopAppBar(
                    title = { Text("Daily Logs")
                            },
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                        }
                    },
                    modifier = Modifier
                        .background(color = Color.Green),
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(5.dp)
        ){
            Text(
                text = "Date: ${selectedDate.value.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                style = MaterialTheme.typography.titleLarge
            )
//            Spacer(modifier = Modifier.height(2.dp))
        }
    }
//fun LogsScreen(navController: NavController) {
//    val bottomSheetState = rememberBottomSheetScaffoldState(
//        bottomSheetState = rememberStandardBottomSheetState(StandardBottomSheetValue.Hidden)
//    )
//    val coroutineScope = rememberCoroutineScope()
//    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
//
//    // BottomSheetScaffold for bottom sheet behavior
//    BottomSheetScaffold(
//        scaffoldState = bottomSheetState,
//        sheetContent = {
//            AddEntryBottomSheet(selectedDate.value) // AddEntryBottomSheet as sheet content
//        },
//        sheetPeekHeight = 0.dp // Ensures the sheet is hidden initially
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Daily Logs") },
//                    navigationIcon = {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
//                        }
//                    },
//                    actions = {
//                        IconButton(onClick = {
//                            coroutineScope.launch { bottomSheetState.bottomSheetState.expand() }
//                        }) {
//                            Icon(Icons.Default.Add, contentDescription = "Add Entry")
//                        }
//                    }
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = {
//                    coroutineScope.launch { bottomSheetState.bottomSheetState.expand() }
//                }) {
//                    Icon(Icons.Default.Add, contentDescription = "Add Entry")
//                }
//            }
//        ) { paddingValues ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(15.dp)
//            ) {
//                // Display selected date
//                Text(
//                    text = "Date: ${selectedDate.value.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Logs content here...
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "Logs will be displayed here", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun AddEntryBottomSheet(selectedDate: LocalDate) {
//    var name by remember { mutableStateOf("") }
//    var hours by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .fillMaxWidth()
//    ) {
//        Text("Date: ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}", style = MaterialTheme.typography.bodyLarge)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = hours,
//            onValueChange = { hours = it },
//            label = { Text("Hours") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Description") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { /* Handle saving of data */ },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Save")
//        }
//    }
//}


//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(15.dp)
//            .background(color = MaterialTheme.colorScheme.surfaceBright),
//        contentAlignment = Alignment.TopStart
//    ) {
//        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(15.dp),
//        contentAlignment = Alignment.TopCenter
//    ) {
//        Text(text = "Daily Logs", style = MaterialTheme.typography.headlineMedium)
//
//
//    }
}
