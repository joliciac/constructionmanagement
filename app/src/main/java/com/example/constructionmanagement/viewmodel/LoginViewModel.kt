package com.example.constructionmanagement.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LoginViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance("https://constructionproject-75d08-default-rtdb.europe-west1.firebasedatabase.app/")

    fun login(email: String, password: String, context: Context, onResult: (Boolean, String?) -> Unit) {
//        if (email.isNotEmpty() && password.isNotEmpty()) {
//            firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        onResult(true)
//                    } else {
//                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
//                        onResult(false)
//                    }
//                }
//        } else {
//            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
//            onResult(false)
//        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.uid?.let { uid ->
                            database.reference.child("users").child(uid).child("role").get()
                                .addOnSuccessListener { snapshot ->
                                    val role = snapshot.getValue(String::class.java)
                                    if (role != null) {
                                        onResult(true,role) // Always navigate to home screen
                                    } else {
                                        Toast.makeText(context, "Login failed. No role assigned", Toast.LENGTH_SHORT).show()
                                        onResult(false, null)
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Login Failed. Could not retrieve user role", Toast.LENGTH_LONG).show()
                                    onResult(false, null)
                                }
                        }
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                        onResult(false, null)
                    }
                }
        } else {
            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            onResult(false, null)
        }
    }

    fun showForgotPasswordDialog() {
        // Implementation for a forgot password dialog
    }
}
