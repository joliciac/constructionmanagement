package com.example.constructionmanagement.composables.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.constructionmanagement.ui.theme.PurpleGrey80
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var animationStart by remember { mutableStateOf(false)}
    val alphaAnim = animateFloatAsState(
        targetValue = if(animationStart) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )
    LaunchedEffect(Unit) {
        animationStart = true
        delay(4000L)
        navController.navigate("home") {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }
    Splash(alpha = alphaAnim.value)
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center,
//    ) {
//        BasicText(
//            text = "Your Management System",
//            style = MaterialTheme.typography.titleMedium.copy(
//                fontWeight = FontWeight.Bold,
//                fontSize = 30.sp
//            )
//        )
//    }
}

@Composable
fun Splash(alpha: Float){
    Box(modifier = Modifier
        .background(if (isSystemInDarkTheme()) Color.Black else PurpleGrey80)
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
