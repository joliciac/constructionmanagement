package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit, onNavigateToSignup: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val loginViewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login",
                fontSize = 38.sp,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .padding(25.dp),
            verticalArrangement = Arrangement.Bottom,
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
            }) {
                if (isLoading) CircularProgressIndicator(color = Color(0xFF351D43)) else Text("Login")
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
