package com.example.constructionmanagement.composables.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.constructionmanagement.R
import com.example.constructionmanagement.viewmodel.SignupViewModel

@Composable
fun SignupScreen(onSignupSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val roles = listOf("Worker", "Admin")
    var selectedRole by remember { mutableStateOf(roles[0]) }
    val signupViewModel: SignupViewModel = viewModel()
    val context = LocalContext.current

    val bungeeFontFamily = FontFamily(Font(R.font.bungee_shade))

    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.43f),
                painter = painterResource(id = R.drawable.shape2),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
    }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                "Sign up",
                fontSize = 50.sp,
                fontFamily = bungeeFontFamily,
                color = MaterialTheme.colorScheme.scrim
            )
            Card(
                modifier = Modifier
                    .fillMaxHeight(0.65f)
                    .padding(25.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .padding(bottom = 15.dp)
                            .fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .fillMaxWidth(),
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))

                Text("Select your role: ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Left) {
                    roles.forEach{ role ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(3.dp)
                        ) {
                            RadioButton(
                                selected = selectedRole == role,
                                onClick = { selectedRole = role},
                                colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.tertiary)
                            )
                            Text(role)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    isLoading = true
                    signupViewModel.signup(email, password, confirmPassword, selectedRole, context) { isSuccess ->
                        isLoading = false
                        if (isSuccess) {
                            onSignupSuccess()
                        }
                    }
                },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
                ) {
                    if (isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.surfaceTint) else Text("Signup")
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login",
                        modifier = Modifier.padding(15.dp),
                        color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
}

@Preview
@Composable
fun SignupScreenPreview(){
    SignupScreen(
        onSignupSuccess = {}
    ) {
    }
}