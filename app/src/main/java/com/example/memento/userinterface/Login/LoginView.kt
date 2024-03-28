package org.example.userinterface.Login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memento.theme.MementoTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    toHomePage: () -> Unit = {}
) {
    val auth: FirebaseAuth = Firebase.auth

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var signUpError by remember { mutableStateOf<String?>(null) }

    fun signUp(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            signUpError = "Username and Password cannot be blank"
        } else {
            auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toHomePage()
                } else {
                    // Sign up failed
                    val exception = task.exception
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    signUpError = if (exception is FirebaseAuthException) {
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
        toHomePage()
        if (username.isEmpty() || password.isEmpty()) {
            signUpError = "Username and Password cannot be blank"
        } else {
            auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toHomePage()
                } else {
                    // Sign in failed
                    val exception = task.exception
                    signUpError = if (exception is FirebaseAuthException) {
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

    MementoTheme {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "MEMENTO",
                            textAlign = TextAlign.Center,
                            fontSize = 65.sp,
                            lineHeight = 33.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    },
                    modifier = Modifier.padding(top = 180.dp, bottom = 100.dp)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                TextField(
                    username,
                    colors = TextFieldDefaults.textFieldColors(focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
                    label = { Text("Username: ") },
                    onValueChange = { username = it },

                    )

                TextField(
                    password,
                    colors = TextFieldDefaults.textFieldColors(focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
                    label = { Text("Password: ") },
                    onValueChange = { password = it },

                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Row() {
                    Button(
                        onClick = { signIn(username, password) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(12.dp),
                    ) { Text(" Log In ", fontSize = 22.sp) }

                }
                Text("or", fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                Row() {
                    Button(
                        onClick = { signUp(username, password) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(top = 12.dp),
                    ) {
                        Text("Sign Up", fontSize = 22.sp)
                    }
                }
                Row (){
                    signUpError?.let { error ->
                        Text(
                            text = error,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }
    }
}