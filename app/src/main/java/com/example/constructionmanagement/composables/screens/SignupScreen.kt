package com.example.constructionmanagement.composables.screens

import android.widget.CheckedTextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
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

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                "Sign up",
                fontSize = 38.sp,
                modifier = Modifier
                    .padding(top = 60.dp),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(35.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(25.dp),
                verticalArrangement = Arrangement.Bottom) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier
                        .padding(bottom = 15.dp),
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password")},
//                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(25.dp))

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
                                onClick = { selectedRole = role}
                            )
                            Text(role)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    isLoading = true
                    signupViewModel.signup(email, password, confirmPassword, selectedRole, context) { isSuccess ->
                        isLoading = false
                        if (isSuccess) {
                            onSignupSuccess()
                        }
                    }
                }) {
                    if (isLoading) CircularProgressIndicator(color = Color.White) else Text("Signup")
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login",
                        modifier = Modifier.padding(15.dp))
                }
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