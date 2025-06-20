package com.example.constructionmanagement.composables.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import com.example.constructionmanagement.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.viewmodel.FCMViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    onNotificationClick: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onLogoutClick: () -> Unit,
    fcmViewModel: FCMViewModel = viewModel(),
    onDeleteAccountClick: () -> Unit
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    val auth = FirebaseAuth.getInstance()
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    CheckNotificationPermission()

    val onDeleteConfirmed = {
        onDeleteAccountClick()
        showDeleteConfirmationDialog = false
    }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(
                icon = Icons.Default.Settings,
                title = stringResource(R.string.settings_title),
                onIconClick = {}
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SettingsOptionRow(
                        iconPainter = painterResource(id = R.drawable.notifications_24px),
                        title = stringResource(id = R.string.notification_opt),
                        onClick = { onNotificationClick(notificationsEnabled) }
                    )
                    Switch(
                        modifier = Modifier.fillMaxWidth(),
                        checked = notificationsEnabled,
                        onCheckedChange = { isChecked ->
                            notificationsEnabled = isChecked

                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                fcmViewModel.updateNotification(userId, notificationsEnabled)

                                if (notificationsEnabled) {
                                    fcmViewModel.storeFCMToken()
                                }
                            } else {
                                Log.e("Settings", "User not authenticated")
                            }
                            onNotificationClick(isChecked)
                        }
                    )
                }
                SettingsOptionRow(
                    iconPainter = painterResource(id = R.drawable.globe_asia_24px),
                    title = stringResource(id = R.string.change_language_opt),
                    onClick = { showLanguageDialog = true }
                )
                SettingsOptionRow(
                    iconPainter = if (isDarkTheme) {
                        painterResource(id = R.drawable.dark_mode__filled_24px)
                    } else {
                        painterResource(id = R.drawable.dark_mode_24px)
                    },
                    title = stringResource(id = R.string.dark_mode_theme),
                    onClick = onThemeClick
                )
                SettingsOptionRow(
                    iconPainter = painterResource(id = R.drawable.logout_24px),
                    title = stringResource(id = R.string.logout),
                    onClick = onLogoutClick
                )
                Spacer(modifier = Modifier.height(40.dp))
                SettingsOptionRow(
                    iconPainter = painterResource(id= R.drawable.delete_forever_24dpx),
                    title = stringResource(id = R.string.permanently_delete),
                    onClick = { showDeleteConfirmationDialog = true }
                )
            }

            if (showLanguageDialog){
                LangSelectionDialogue(
                    onDismiss = { showLanguageDialog = false },
                    onLanguageSelected = { language -> onLanguageChange(language)
                    showLanguageDialog = false
                    }
                )
            }
            if (showDeleteConfirmationDialog) {
                PermanentlyDelete(
                    onDismiss = { showDeleteConfirmationDialog = false },
                    onDeleteConfirmed = onDeleteConfirmed
                )
            }
        }
    }
}

@Composable
fun ScreenHeader(
    painter: Painter? = null,
    icon: ImageVector? = null,
    title: String,
    onIconClick: (() -> Unit)? = null) {
    val bungeeShade = FontFamily(Font(R.font.bungee_shade))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 20.dp)
    ) {
        IconButton(
            onClick = { onIconClick?.invoke() },
            modifier = Modifier
                .size(48.dp)
                .padding(end = 8.dp)
        ) {
            if (painter != null){
                Icon(
                    painter = painter,
                    contentDescription = "$title Icon",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            } else if (icon != null){
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp),
            fontFamily = bungeeShade,
            fontWeight = FontWeight.Thin
        )
    }
}

@Composable
fun SettingsOptionRow(
    iconPainter: Painter,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LangSelectionDialogue(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
){
    val languages = listOf("English", "Spanish", "Polish", "Romanian", "Lithuanian", "Portuguese")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.select_language))
        },
        text = {
            Column {
                languages.forEach { language ->
                    Text(
                        text = language,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Composable
fun CheckNotificationPermission() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("FCM", "Notification permission granted")
        } else {
            Log.d("FCM", "Notification permission denied")
        }
    }

    // LaunchedEffect to check & request permission on screen load
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun PermanentlyDelete(
    onDismiss:() -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Account Confirmation") },
        text = {
            Text(
                "Are you sure you want to permanently delete your account?"
            )
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirmed) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        isDarkTheme = false,
        onThemeClick = {},
        onNotificationClick = {},
        onLanguageChange = {},
        onLogoutClick = {},
        onDeleteAccountClick = {})
}