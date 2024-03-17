package org.example.userinterface.Home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImage
import com.example.memento.BuildConfig
import com.example.memento.theme.MementoTheme
import java.io.File
import java.time.LocalDateTime
import java.util.Date
import java.util.Objects
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeView(
        onHomeClicked: () -> Unit = {},
) {
    MementoTheme {
        val context = LocalContext.current
        val file = context.createImageFile()
        val uri =
                FileProvider.getUriForFile(
                        Objects.requireNonNull(context),
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                )
        Log.d("HomeView", "Uri: ${BuildConfig.APPLICATION_ID}.provider")

        var dailyPrompt = "Daily prompt"

        var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

        val cameraLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isTaken
                    ->
                    if (isTaken) {
                        val imageFile = context.createImageFile()
                        Log.d("HomeView", "Image file created at ${imageFile.absolutePath}")
                        capturedImageUri = Uri.fromFile(imageFile)
                        Log.d("HomeView", "Image captured at ${capturedImageUri?.path}")
                    } else {
                        Log.e("HomeView", "Failed to capture image")
                        Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT)
                                .show()
                    }
                }

        val permissionLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (it) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                        cameraLauncher.launch(uri)
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                    }
                }

        var daysPressed by remember { mutableIntStateOf(5) }
        var hoursLeft by remember { mutableIntStateOf(24) }
        var minutesLeft by remember { mutableIntStateOf(0) }

        LaunchedEffect(true) {
            val firstDateTime = LocalDateTime.now()
            if (firstDateTime.minute == 0){
                hoursLeft = 24 - firstDateTime.hour
                minutesLeft = 0
            } else {
                hoursLeft = 23 - firstDateTime.hour
                minutesLeft = 60 - firstDateTime.minute
            }
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
                    modifier =
                            Modifier.fillMaxSize()
                                    .padding(innerPadding)
                                    .background(MaterialTheme.colorScheme.background)
            ) {

                // Streak and time left
                item {
                        Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(vertical = 2.5.dp, horizontal = 50.dp)
                        ) {
                            Card(
                                    colors =
                                            CardDefaults.cardColors(
                                                    containerColor =
                                                            MaterialTheme.colorScheme.secondary,
                                            ),
                                    modifier = Modifier.padding(20.dp).weight(1f).heightIn(max = 200.dp)
                            ) {
                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                            painter =
                                                    painterResource(
                                                            id =
                                                                    com.example
                                                                            .memento
                                                                            .R
                                                                            .drawable
                                                                            .ic_lightning
                                                    ),
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
                                    colors =
                                            CardDefaults.cardColors(
                                                    containerColor =
                                                            MaterialTheme.colorScheme.secondary,
                                            ),
                                    modifier = Modifier.padding(20.dp).weight(1f).heightIn(max = 200.dp)
                            ) {
                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                            painter =
                                                    painterResource(
                                                            id = com.example.memento.R.drawable.ic_clock
                                                    ),
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
                    
                if (capturedImageUri != null) {
                        item {
                                Card(
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onBackground,
                                        ),
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Card(
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                        ),
                                modifier = Modifier.fillMaxWidth().padding(1.dp)
                            ){
                            // Display captured image
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Log.d("HomeView", "Displaying captured image: $capturedImageUri")
                                Card(
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.onBackground,
                                                ),
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp).height(300.dp)
                                ) {
                                        AnimatedVisibility(
                                                visible = capturedImageUri.toString().isNotEmpty()){
                                    AsyncImage(
                                        model = capturedImageUri.toString(),
                                        contentDescription = "captured memento",
                                        modifier =
                                        Modifier.fillMaxWidth()
                                            .padding(
                                                PaddingValues(top = 20.dp, bottom = 100.dp)
                                            )
                                    )}
                                                }
                                // Contenido
                                Text(
                                        "Memento captured:\n$dailyPrompt",
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                        }
                            }
                        }
        }
        else {
                // Daily prompt
                item {
                        Card(
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.secondary,
                                        ),
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Text(
                                    dailyPrompt,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp)
                            )
                        }
                    }
                // Capture memento
                item {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    FloatingActionButton(
                                            onClick = {
                                                    val permissionCheckResult =
                                                            ContextCompat.checkSelfPermission(
                                                                    context,
                                                                    Manifest.permission.CAMERA
                                                            )
                                                    if (permissionCheckResult ==
                                                                    PackageManager.PERMISSION_GRANTED
                                                    ) {
                                                    cameraLauncher.launch(uri)
                                                    } else {
                                                    // Request a permission
                                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                                    }
                                            },
                                            containerColor = MaterialTheme.colorScheme.onBackground,
                                            contentColor = MaterialTheme.colorScheme.onPrimary,
                                    ) { Icon(Icons.Filled.Create, "capture memento") }
    
                                    Spacer(modifier = Modifier.height(7.dp))
    
                                    Text(
                                            "CAPTURE MEMENTO",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                    )
                                    }
                            }
                    }
                item {
                Image(
                        painter =
                                painterResource(id = com.example.memento.R.drawable.`when`),
                        contentDescription = "when is your... memento :)?",
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(
                                                PaddingValues(top = 20.dp, bottom = 100.dp)
                                        )
                )
                }
            }

            }
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image =
            File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    externalCacheDir /* directory */
            )
    return image
}
