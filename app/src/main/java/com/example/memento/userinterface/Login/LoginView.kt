package org.example.userinterface.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    onLoginButtonClicked: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                    title = { Text("MEMENTO", textAlign = TextAlign.Center, fontSize = 65.sp, lineHeight = 33.sp)

                            },
                    modifier = Modifier.padding(top=180.dp, bottom=100.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {

            TextField(
                username,
                label = {Text("Username: ")},
                onValueChange = { username = it },

                    )

            TextField(
                password,
                label = {Text("Password: ")},
                onValueChange = { password = it },

                visualTransformation =  PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

            Row () {
                Button(
                        onClick = onLoginButtonClicked,
                        modifier = Modifier.padding(12.dp),
                        ) {Text(" Log In ", fontSize = 22.sp)}

            }
            Text("or", fontSize = 15.sp)
            Row () {
                Button(onClick = onLoginButtonClicked, modifier = Modifier.padding(top=12.dp),) {
                    Text("Sign Up", fontSize = 22.sp)
                }
            }
        }
    }
}