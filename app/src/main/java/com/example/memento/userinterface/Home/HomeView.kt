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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.memento.BuildConfig
import com.example.memento.mvvm.viewmodel.DiscoverViewModel
import com.example.memento.theme.MementoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.time.LocalDateTime
import java.util.Date
import java.util.Objects
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun getCurrentTimeAsString(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Define the format you want
    return currentTime.format(formatter)
}

@Composable
fun ItemWithToggleAndButton(
    public : Boolean
) {
    val viewModel = DiscoverViewModel()
    var isPublic by remember { mutableStateOf(public) }
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
    val db = Firebase.firestore
    val documentPath = "${currentUser?.uid}_${today}.jpg"

    LaunchedEffect(isPublic) {
        db.collection("posts").document(documentPath)
            .update("public", isPublic)
            .addOnSuccessListener {
                Log.e("DOC PUBLIC", "Successfully updated public boolean")
            }
            .addOnFailureListener { e ->
                Log.e("ItemWithToggleAndButton", "Failed to update document", e)
            }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Row containing switch and button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Toggle with label "Public"
            Text(text = "Public", color = Color.Black, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isPublic,
                onCheckedChange = { isPublic = it }
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Button with "X" label
            Button(onClick = {
                viewModel.deleteDocumentAndImage(documentPath)

            }) {
                Text(text = "X")
            }
        }
    }
}

@Composable
fun ChatItem(
    captionText : String
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
    val db = Firebase.firestore
    var message by remember { mutableStateOf(captionText) }
    Log.e("CHAT TEXT", captionText)

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (currentUser != null) {
                    val postData = hashMapOf(
                        "caption" to message
                    )
                    db.collection("posts").document("${currentUser.uid}_${today}.jpg")
                        .set(postData, SetOptions.merge())
                        .addOnSuccessListener { Log.d("POSTS WRITE", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }
                }
                      },
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "Send")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeView(
        onHomeClicked: () -> Unit = {},
) {
    MementoTheme {
        val viewModel = DiscoverViewModel()

        val context = LocalContext.current
        val file = context.createImageFile()
        val uri =
                FileProvider.getUriForFile(
                        Objects.requireNonNull(context),
                        BuildConfig.APPLICATION_ID + ".provider",
                        file
                )
        val db = Firebase.firestore

        var dailyPrompt by remember { mutableStateOf("Daily prompt") }


        var caption by remember { mutableStateOf("") }
        var public by remember { mutableStateOf(false)}
        var isFetched by remember { mutableStateOf(false) }


        val prompts = db.collection("prompts")

        // Firebase
        val storage = Firebase.storage
        val imagesRef = storage.reference.child("images")
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

        var imageAvailable by remember { mutableStateOf(false) }
        val vmImageAvailable by viewModel.imageAvailable
                LaunchedEffect(vmImageAvailable) {
                    imageAvailable = vmImageAvailable
                }

        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
        var imageRef = imagesRef.child("null.jpg")
        if (currentUser !== null) {
            val userUid = currentUser.uid
            imageRef = imagesRef.child("${userUid}_${today}.jpg")
            Log.e("USER UID FIREBASE", currentUser.uid)
        }

        if (currentUser != null) {
            db.collection("posts").document("${currentUser.uid}_${today}.jpg")
                .get()
                .addOnSuccessListener { document ->
                    val fbCaption = document.getString("caption")
                    val fbPublic  = document.getBoolean("public")
                    if ( fbCaption !== null && fbPublic !== null) {
                        caption = fbCaption
                        public = fbPublic
                        isFetched = true
                    }

                }
                .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }
        }

        prompts.whereEqualTo("timestamp", today)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val prompt = document.getString("prompt")
                    if (prompt != null) {
                        dailyPrompt = prompt
                    } else {
                        Log.e("POSTS READ", "Prompt field is null")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("POSTS READ", "Error getting documents", e)
            }

        imageRef.downloadUrl.addOnSuccessListener {
            Log.d("HomeView", "Image available at $it")
            imageAvailable = true
            viewModel.setImageAvailable(true)
            capturedImageUri = it
        }.addOnFailureListener {
            Log.e("HomeView", "Image not available")
            imageAvailable = false
            viewModel.setImageAvailable(false)
        }

        val cameraLauncher =
                rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isTaken
                    ->
                    if (isTaken && currentUser !== null) {
                        val userUid = currentUser.uid
                        Log.d("HomeView", "Image captured successfully")
                        capturedImageUri = uri
                        val todayImageRef = imagesRef.child("${userUid}_${today}.jpg")
                        Log.d("HomeView", "Uploading image to $todayImageRef")
                        Log.d("HomeView", "Captured image uri: $capturedImageUri")
                        capturedImageUri?.let { uri ->
                            val imageStream = context.contentResolver.openInputStream(uri)
                            Log.d("HomeView", "Image stream: $imageStream")
                            imageStream?.let { stream ->
                                todayImageRef.putStream(stream)
                                    .addOnSuccessListener {
                                        Log.d("HomeView", "Image uploaded successfully")
                                        imageAvailable = true

                                        val postsCollection = FirebaseFirestore.getInstance()
                                        val db = Firebase.firestore
                                        val postData = hashMapOf(
                                            "public" to true,
                                            "date" to today,
                                            "time" to getCurrentTimeAsString(),
                                            "userid" to userUid,
                                            "caption" to ""
                                        )

                                        db.collection("posts").document("${userUid}_${today}.jpg")
                                            .set(postData)
                                            .addOnSuccessListener { Log.d("POSTS WRITE", "DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("HomeView", "Failed to upload image: ${e.message}")
                                    }
                            }
                        }
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
                            modifier = Modifier.padding(
                                PaddingValues(top = 1.dp, bottom = 10.dp))
                    )
                }
        ) { innerPadding ->
            LazyColumn(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
            ) {
                // Streak and time left
                item {
                        Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.5.dp, horizontal = 50.dp)
                        ) {
                            Card(
                                    colors =
                                            CardDefaults.cardColors(
                                                    containerColor =
                                                            MaterialTheme.colorScheme.secondary,
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
                    
                if (imageAvailable) {

                        item {
                                Card(
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onBackground,
                                        ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 25.dp)
                        ) {
                            Card(
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                        ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(1.dp)
                            ){
                            if (isFetched) {
                                ItemWithToggleAndButton(public)
                            }
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
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .height(550.dp)
                                ) {
                                        Image(
                                                painter = rememberImagePainter(capturedImageUri),
                                                contentDescription = "Today's Memento",
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .align(Alignment.CenterHorizontally)
                                        )
                                                }
                                // Contenido
                                if (isFetched) {
                                    ChatItem(caption)
                                }
                                }
                        }
                            }
                        }
                        item {
                                Spacer(
                                        modifier = Modifier.height(75.dp)
                                )
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp), contentAlignment = Alignment.Center,) {
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
                        Modifier
                            .fillMaxWidth()
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


//TODO: add public/private toggle and delete post function,m