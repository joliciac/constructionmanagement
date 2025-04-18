package com.example.constructionmanagement.composables.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.constructionmanagement.ui.theme.md_theme_dark_primary
import com.example.constructionmanagement.ui.theme.md_theme_light_primary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var animationStart by remember { mutableStateOf(false)}
    val alphaAnim = animateFloatAsState(
        targetValue = if(animationStart) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        ), label = ""
    )
    LaunchedEffect(Unit) {
        animationStart = true
        delay(4000L)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
        }
    } else {
        navController.navigate("login"){
            popUpTo("splash") {inclusive = true}
        }
    }
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float){
    Box(modifier = Modifier
        .background(if (isSystemInDarkTheme()) md_theme_dark_primary else md_theme_light_primary)
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(120.dp)
                .alpha(alpha = alpha),
            imageVector = Icons.Default.DateRange,
            contentDescription = "Logo Icon",
            tint = Color.White
        )
    }
}

@Composable
@Preview
fun SplashScreenPreview() {
    Splash(alpha = 1f)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun SplashScreenPreviewDark() {
    Splash(alpha = 1f)
}
