@file:OptIn(ExperimentalMaterial3Api::class)
package org.burgerbytes.userinterface.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burgerbytes.memento.theme.MementoTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SwitchDefaults
import com.burgerbytes.memento.mvvm.viewmodel.SettingsViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.burgerbytes.memento.R


@Composable
fun Popup(onDismiss: () -> Unit, text: String) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .size(400.dp),
            color = MaterialTheme.colorScheme.onBackground,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    onLogoutClicked: () -> Unit = {},
    isDarkMode: Boolean,
    toggleDarkMode: (Boolean) -> Unit,
    viewModel: SettingsViewModel = SettingsViewModel()
) {

    val scrollState = rememberScrollState()
    var showPrivacyPopup by remember { mutableStateOf(false) }
    var showTermsOfService by remember { mutableStateOf(false) }
    val openResourcesUrlLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    MementoTheme(darkTheme = isDarkMode) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(stringResource(id = R.string.settings), textAlign = TextAlign.Center, fontSize = 40.sp
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
                    .verticalScroll(scrollState)
            ) {
                Divider(thickness = 3.dp)


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

                Column() {
                    Text(
                        "Language", fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        "The app's language can be changed between Englsih and Spanish in Android settings",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    )

                    Divider(thickness = 1.dp)
                }

                Column() {
                    Text(
                        stringResource(id = R.string.m_h_resources), fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Button(
                        onClick = { openResourcesUrlLauncher.launch(viewModel.mhResourceLink)},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(top = 12.dp).padding(horizontal = 25.dp),
                    ) {
                        Text(stringResource(id = R.string.o_resources), fontSize = 14.sp)
                    }
                    Button(
                        onClick = { openResourcesUrlLauncher.launch(viewModel.mhPhoneLink)},
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text(stringResource(id = R.string.m_h_phone), fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                }

                Column() {
                    Text(
                        stringResource(id = R.string.legal), fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Button(
                        onClick = { showPrivacyPopup = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text(stringResource(id = R.string.privacy_policy), fontSize = 14.sp)
                    }

                    if (showPrivacyPopup) {
                        Popup(onDismiss = { showPrivacyPopup = false}, text = stringResource(id = R.string.privacy_policy_paragraph))
                    }

                    Button(
                        onClick = { showTermsOfService = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .padding(horizontal = 25.dp),
                    ) {
                        Text(stringResource(id = R.string.t_o_s), fontSize = 14.sp)
                    }

                    if (showTermsOfService) {
                        Popup(onDismiss = { showTermsOfService= false}, text = stringResource(id = R.string.t_o_s_paragraph))
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
                        Text(stringResource(id = R.string.log_out), fontSize = 14.sp)
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
                        Text(stringResource(id = R.string.delete_account), fontSize = 14.sp)
                    }
                    Divider(thickness = 1.dp)
                }

            }
        }
    }
}