package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.constructionmanagement.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onLanguageChange: () -> Unit
) {
    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(
                icon = Icons.Default.Settings, title = "Settings", onIconClick = {}
            )
            Spacer(modifier = Modifier.height(32.dp))
            SettingsOptionRow(
                iconPainter = painterResource(id = R.drawable.notifications_24px),
                title = "Notifications",
                onClick = onNotificationClick
            )
            SettingsOptionRow(
                iconPainter = painterResource(id = R.drawable.globe_asia_24px),
                title = "Change Language",
                onClick = onLanguageChange
            )
            SettingsOptionRow(
                iconPainter = if (isDarkTheme) {
                    painterResource(id = R.drawable.dark_mode__filled_24px)
                } else {
                     painterResource(id = R.drawable.dark_mode_24px)
                },
                title = "Dark Mode",
                onClick = onThemeClick
            )
//            SettingsOptionRow(
//                icon = Icons.Default.Info,
//                title = "About",
//                onClick = { /* Navigate to About Screen */ }
//            )
        }
    }
}

@Composable
fun ScreenHeader(
    painter: Painter? = null,
    icon: ImageVector? = null,
    title: String,
    onIconClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
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
                    tint = MaterialTheme.colorScheme.primary
                )
            } else if (icon != null){
                Icon(
                    imageVector = icon,
                    contentDescription = "$title Icon",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp)
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
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        isDarkTheme = false,
        onThemeClick = {},
        onNotificationClick = {},
        onLanguageChange = {})
}