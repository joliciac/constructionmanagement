package com.example.constructionmanagement.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app/")
    private val usersRef = database.getReference("users")

    fun signup(
        email: String,
        password: String,
        confirmPassword: String,
        role: String,
        context: Context,
        onResult: (Boolean) -> Unit
    ){
        if (email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()){
            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                onResult(false)
                return
            }

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                        val userMap = mapOf("email" to email, "role" to role)
                        usersRef.child(uid).setValue(userMap)
                            .addOnSuccessListener {
                                onResult(true)
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to saver user role", Toast.LENGTH_SHORT).show()
                                onResult(false)
                            }
                    } else {
                        Toast.makeText(context, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        onResult(false)
                    }
                }
        } else {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            onResult(false)
        }
    }
}