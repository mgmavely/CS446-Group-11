@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.userinterface.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memento.theme.MementoTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SwitchDefaults
import com.example.memento.mvvm.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.compose.ui.res.stringResource
import com.example.memento.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsView(
    onMentalHealthOnlineClicked: () -> Unit = {},
    onMentalHealthPhoneClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit = {},
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
    viewModel: SettingsViewModel = SettingsViewModel()

) {
    val auth = FirebaseAuth.getInstance()

    MementoTheme(darkTheme = isDarkMode) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("SETTINGS", textAlign = TextAlign.Center, fontSize = 40.sp
                        ,color = MaterialTheme.colorScheme.onBackground,)

                    },
                    modifier = Modifier.padding(25.dp)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Divider(thickness = 3.dp)

                var checked by remember { mutableStateOf(true) }


                Column() {
                    Text(
                        stringResource(id = R.string.dark_mode), fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Switch(
                        checked = isDarkMode,
                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(horizontal = 25.dp),
                        onCheckedChange = {
                            toggleDarkMode(!isDarkMode)
                        }
                    )
                    Divider(thickness = 1.dp)
                }

                Column(modifier = Modifier) {
                    Text(
                        "Notifications", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Text(
                        "Enable Notifications", fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Switch(
                        checked = checked,
                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(horizontal = 25.dp),
                        onCheckedChange = {
                            checked = it
                        }
                    )

                    Text(
                        "Temporarily Mute Notifications", fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Switch(
                        checked = false,
                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(horizontal = 25.dp),
                        onCheckedChange = {
                            checked = it
                        }
                    )

                    Divider(thickness = 1.dp)
                }

                Column() {
                    Text(
                        "Mental Health Resources", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Button(
                        onClick = onMentalHealthOnlineClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(top = 12.dp).padding(horizontal = 25.dp),
                    ) {
                        Text("Online Mental Health Resources", fontSize = 14.sp)
                    }
                    Button(
                        onClick = onMentalHealthPhoneClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text("Mental Health Related Phone Lines", fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                }

                Column() {
                    Text(
                        "Legal", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Button(
                        onClick = onMentalHealthOnlineClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text("Privacy Policy", fontSize = 14.sp)
                    }
                    Button(
                        onClick = onMentalHealthPhoneClicked,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text("Terms Of Service", fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                }

                Column() {
                    Button(
                        onClick = {
                            viewModel.auth.signOut() // Sign out the user
                            onLogoutClicked() // Execute the callback function
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                    ) {
                        Text("Log Out", fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                    Button(
                        onClick = {
                            val user = viewModel.auth.currentUser
                            user?.delete()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Account deleted successfully
                                    viewModel.auth.signOut()
                                    onLogoutClicked()
                                } else {
                                    // Failed to delete account
                                    Log.e("SettingsView", "Failed to delete account: ${task.exception}")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                    ) {
                        Text("Delete Account", fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                }

            }
        }
    }
}