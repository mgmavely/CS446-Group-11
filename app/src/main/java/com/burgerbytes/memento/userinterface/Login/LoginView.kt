package org.burgerbytes.userinterface.Login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burgerbytes.memento.mvvm.viewmodel.LoginViewModel
import com.burgerbytes.memento.theme.MementoTheme
import com.burgerbytes.memento.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    isDarkMode: Boolean,
    toHomePage: () -> Unit = {},
    viewModel: LoginViewModel = LoginViewModel()
) {
    viewModel.setHomeFn(toHomePage)

    val logIn = stringResource(id = R.string.log_in)
    val signUp = stringResource(id = R.string.sign_up)
    val or = stringResource(id = R.string.or)

    MementoTheme(darkTheme = isDarkMode) {

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
                    viewModel.username.value,
                    colors = TextFieldDefaults.textFieldColors(focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
                    label = { Text("Username: ") },
                    onValueChange = { viewModel.username.value = it },

                    )

                TextField(
                    viewModel.password.value,
                    colors = TextFieldDefaults.textFieldColors(focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
                    label = { Text("Password: ") },
                    onValueChange = { viewModel.password.value = it },

                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                Row() {
                    Button(
                        onClick = { viewModel.signIn(viewModel.username.value, viewModel.password.value) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(12.dp),
                    ) { Text(logIn, fontSize = 22.sp) }

                }
                Text(or, fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                Row() {
                    Button(
                        onClick = { viewModel.signUp(viewModel.username.value, viewModel.password.value) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(top = 12.dp),
                    ) {
                        Text(signUp, fontSize = 22.sp)
                    }
                }
                Row (){
                    viewModel.signUpError.value?.let { error ->
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