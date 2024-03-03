package org.example.userinterface.Home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.memento.theme.MementoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeView(
    onHomeClicked: () -> Unit = {}
) {
    MementoTheme {
        var daysPressed by remember { mutableIntStateOf(5) }
        var hoursLeft by remember { mutableIntStateOf(24) }
        var minutesLeft by remember { mutableIntStateOf(0) }

        LaunchedEffect(true) {
            while (true) {
                val currentDateTime = LocalDateTime.now()
                if (currentDateTime.hour == 0 && currentDateTime.minute == 0) {
                    hoursLeft = 24
                    minutesLeft = 0
                } else {
                    if (minutesLeft == 0) {
                        minutesLeft = 59
                        hoursLeft--
                    } else {
                        minutesLeft--
                    }
                    if (hoursLeft == 0 && minutesLeft == 0) {
                        hoursLeft = 24
                        minutesLeft = 0
                    }
                }
                delay(60000)
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "MEMENTO",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier.padding(1.dp)
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Daily prompt",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                vertical = 15.dp,
                                horizontal = 10.dp
                            )
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.5.dp, horizontal = 50.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                            modifier = Modifier
                                .padding(20.dp)
                                .weight(1f)
                                .heightIn(max = 200.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.memento.R.drawable.ic_lightning),
                                    contentDescription = "streak",
                                    modifier = Modifier.size(100.dp)
                                )
                                Text(
                                    "$daysPressed \ndays streak",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            ),
                            modifier = Modifier
                                .padding(20.dp)
                                .weight(1f)
                                .heightIn(max = 200.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.memento.R.drawable.ic_clock),
                                    contentDescription = "time left",
                                    modifier = Modifier.size(100.dp)
                                )
                                Text(
                                    "$hoursLeft:${
                                        minutesLeft.toString().padStart(2, '0')
                                    }\n time left",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                item {
                    Image(
                    painter = painterResource(id = com.example.memento.R.drawable.`when`),
                    contentDescription = "when is your... memento :)?",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 10.dp)
                )


                }
            }
        }
    }
}