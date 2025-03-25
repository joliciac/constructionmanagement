package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.R
import com.example.constructionmanagement.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit, onNavigateToSignup: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val loginViewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    val bungeeFontFamily = FontFamily(Font(R.font.bungee_shade))

    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.43f),
                painter = painterResource(id = R.drawable.shape),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login",
                fontSize = 50.sp,
                fontFamily = bungeeFontFamily,
                color = MaterialTheme.colorScheme.scrim
            )
            Card(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .padding(25.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    isLoading = true
                    loginViewModel.login(email, password, context) { isSuccess, role ->
                        isLoading = false
                        if (isSuccess && role != null) {
                            onLoginSuccess(role)
                        }
                    }
                },
//                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                ) {
                    if (isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.surface) else Text(
                        "Login"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { loginViewModel.showForgotPasswordDialog() }) {
                    Text("Forgot Password?")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onNavigateToSignup) {
                    Text("Don't have an account? Sign up")
                }
            }
    }
}


@Preview
@Composable
fun LoginPreview(){
    LoginScreen(
        onLoginSuccess = {}
    ) {}
}
