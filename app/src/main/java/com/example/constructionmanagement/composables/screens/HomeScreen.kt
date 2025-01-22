package com.example.constructionmanagement.composables.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.constructionmanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(isBottomSheetVisibleOverride: MutableState<Boolean>? = null) {
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(title = "Home Feed", painter = painterResource(id = R.drawable.home_work_24px))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenScaffoldPreview(){
    HomeScreen()
}
