package com.burgerbytes.memento.mvvm.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.*

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class LoginViewModel : ViewModel() {
    val auth: FirebaseAuth = com.google.firebase.Firebase.auth

    var username: MutableState<String> =  mutableStateOf("")
    var password: MutableState<String> =  mutableStateOf("")
    var signUpError: MutableState<String?> = mutableStateOf(null)
    var toHomePage: () -> Unit = {}

    init {

    }

    fun setHomeFn(fn: () -> Unit) {
        toHomePage = fn
    }

    fun signUp(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            signUpError.value = "Username and Password cannot be blank"
        } else {
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toHomePage()
                    } else {
                        // Sign up failed
                        val exception = task.exception
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        signUpError.value = if (exception is FirebaseAuthException) {
                            // Handle specific authentication errors
                            exception.localizedMessage ?: "Unknown error occurred"
                        } else {
                            // Handle generic errors
                            "Sign up failed"
                        }
                    }
                }
        }
    }

    fun signIn(username: String, password: String) {
//        toHomePage()
        if (username.isEmpty() || password.isEmpty()) {
            signUpError.value = "Username and Password cannot be blank"
        } else {
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toHomePage()
                    } else {
                        // Sign in failed
                        val exception = task.exception
                        signUpError.value = if (exception is FirebaseAuthException) {
                            // Handle specific authentication errors
                            exception.localizedMessage ?: "Unknown error occurred"
                        } else {
                            // Handle generic errors
                            "Sign in failed"
                        }
                    }
                }
        }
    }

}