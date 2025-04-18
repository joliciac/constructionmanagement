package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
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

    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }


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
                    .fillMaxHeight(0.67f)
                    .padding(25.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            ) {
                Spacer(modifier = Modifier.height(65.dp))

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
                    if (isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.surface) else Text(
                        "Login"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    forgotEmail = email // Pre-fill if user typed already
                    showForgotDialog = true }) {
                    Text("Forgot Password?")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onNavigateToSignup) {
                    Text("Don't have an account? Sign up")
                }
                if (showForgotDialog) {
                    ForgotPasswordDialog(
                        email = forgotEmail,
                        onEmailChange = { forgotEmail = it },
                        onDismiss = { showForgotDialog = false },
                        onSendReset = {
                            loginViewModel.forgotPassword(forgotEmail, context)
                            showForgotDialog = false
                        }
                    )
                }
            }
    }
}

@Composable
fun ForgotPasswordDialog(
    email: String,
    onEmailChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSendReset: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reset Password") },
        text = {
            Column {
                Text("Reset link will be sent to this email.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }
        },
        confirmButton = {
            Button(onClick = onSendReset) {
                Text("Send Reset Link")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview
@Composable
fun LoginPreview(){
    LoginScreen(
        onLoginSuccess = {}
    ) {}
}
