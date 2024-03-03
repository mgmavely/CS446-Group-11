@file:OptIn(ExperimentalMaterial3Api::class)
package org.example.userinterface.Settings
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsView(
        onMentalHealthOnlineClicked: () -> Unit = {},
        onMentalHealthPhoneClicked: () -> Unit = {}
) {
    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = { Text("Settings", textAlign = TextAlign.Center, fontSize = 40.sp)

                        },
                        modifier = Modifier.padding(25.dp)
                )
            }
    ) { innerPadding ->
        Column(
                modifier = Modifier.fillMaxWidth().padding(innerPadding)
        ) {
            Divider(thickness = 3.dp)

            var checked by remember { mutableStateOf(true) }

            Column () {
                Text("Enable Dark Mode", fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal=10.dp))
                Switch(
                        checked = checked,
                        modifier = Modifier.padding(horizontal=25.dp),
                        onCheckedChange = {
                            checked = it
                        }
                )
                Divider(thickness = 1.dp)
            }


            Column () {
                Text("Mental Health Resources", fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal=10.dp))
                Button(onClick = onMentalHealthOnlineClicked, modifier = Modifier.padding(top=12.dp).padding(horizontal=25.dp),) {
                    Text("Online Mental Health Resources", fontSize = 14.sp)
                }
                Button(onClick = onMentalHealthPhoneClicked, modifier = Modifier.padding(top=5.dp).padding(horizontal=25.dp),) {
                    Text("Mental Health Related Phone Lines", fontSize = 14.sp)
                }
                Divider(thickness = 1.dp)
            }

        }
    }
}
