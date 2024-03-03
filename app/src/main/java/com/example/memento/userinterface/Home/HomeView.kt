package org.example.userinterface.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.util.Objects


class MainActivity : AppCompatActivity(){

    private var pictureIV : ImageView? = null

    private lateinit var photoFile: File
    lateinit var currentPhotoPath: String
    private val PICTURE_FROM_CAMERA: Int = 1
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
@Composable
fun imageCaptureFromCamera(){

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember{
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it){
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }else{
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column (
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                cameraLauncher.launch(uri)
            }else{
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }

        }){
            Text(text = "Capture Image")
        }
    }


    if (capturedImageUri.path?.isNotEmpty() == true){
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = null)
    }else{
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = painterResource(id = com.example.memento.R.drawable.daily_image),
            contentDescription = null)
    }


}

@Preview
@Composable
fun HomeView(
    onHomeClicked: () -> Unit = {}
) {
    var daysPressed by remember { mutableStateOf(5) } // Simulación de días seguidos
    var hoursLeft by remember { mutableStateOf(24) } // Horas para el siguiente prompt
    var minutesLeft by remember { mutableStateOf(0) } // Minutos restantes

    LaunchedEffect(true) {

        while (true) {
            val currentDateTime = LocalDateTime.now()
            if (currentDateTime.hour == 0 && currentDateTime.minute == 0) {
                // Si es medianoche, reinicia el contador
                hoursLeft = 24
                minutesLeft = 0
            } else {
                // Resta una hora si no es medianoche
                if (minutesLeft == 0) {
                    minutesLeft = 59
                    hoursLeft--
                } else {
                    minutesLeft--
                }
                if (hoursLeft == 0 && minutesLeft == 0) {
                    // Si llega a cero, reinicia el contador
                    hoursLeft = 24
                    minutesLeft = 0
                }
            }
            // Espera un minuto antes de actualizar nuevamente
            delay(60000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Home",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            OutlinedTextField(
                value = "Diary prompt",
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center, // Center the columns
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 50.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = com.example.memento.R.drawable.ic_lightning),
                        contentDescription = "streak",
                        modifier = Modifier.size(100.dp)
                    )
                    Text("$daysPressed")
                    Text("Strike of days")
                }
                Spacer(modifier = Modifier.width(80.dp)) // Add spacing between columns
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = com.example.memento.R.drawable.ic_clock),
                        contentDescription = "time remaining",
                        modifier = Modifier.size(100.dp)
                    )
                    Text("$hoursLeft:${minutesLeft.toString().padStart(2, '0')} \n Hours left")
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()

            ) {
                Button(onClick = onHomeClicked)
                {
                    Text("Option #1")
                }
            }
        }
    }
}

