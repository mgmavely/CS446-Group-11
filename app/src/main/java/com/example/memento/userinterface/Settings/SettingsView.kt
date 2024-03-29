@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.userinterface.Settings
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
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
import com.example.memento.theme.MementoTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SwitchDefaults
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsView(
        onMentalHealthOnlineClicked: () -> Unit = {},
        onMentalHealthPhoneClicked: () -> Unit = {},
        onLogoutClicked: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    MementoTheme {

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
                        "Enable Dark Mode", fontSize = 18.sp,
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
                            auth.signOut() // Sign out the user
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
                            val user = auth.currentUser
                            user?.delete()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Account deleted successfully
                                    auth.signOut()
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

