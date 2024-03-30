package com.example.memento.mvvm.viewmodel

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.example.userinterface.Equipment.PostItem
import java.util.Date

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