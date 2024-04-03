package com.example.memento.mvvm.viewmodel
import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.example.userinterface.Equipment.PostItem
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

class HomeViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts: StateFlow<List<PostItem>> = _posts

    val prompts = db.collection("prompts")
    val imagesRef = storage.reference.child("images")
    val currentUser = firebaseAuth.currentUser

    var imageRef = imagesRef.child("null.jpg")
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())

    val imageAvailable: MutableState<Boolean> = mutableStateOf(false)
    var dailyPrompt: MutableState<String> = mutableStateOf("")
    var capturedImageUri: Uri? = null

    var caption: MutableState<String> = mutableStateOf("")
    var public: MutableState<Boolean> = mutableStateOf(false)
    var isFetched: MutableState<Boolean> = mutableStateOf(false)

    val documentPath = "${currentUser?.uid}_${today}.jpg"
    var isTaken: MutableState<Boolean> = mutableStateOf(false)

    init {
        loadPosts()

        if (currentUser !== null) {
            val userUid = currentUser.uid
            imageRef = imagesRef.child("${userUid}_${today}.jpg")
            Log.e("USER UID FIREBASE", currentUser.uid)
        }

        prompts.whereEqualTo("timestamp", today)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val prompt = document.getString("prompt")
                    if (prompt != null) {
                        dailyPrompt.value = prompt
                        Log.e("Step 1", dailyPrompt.value)
                        //refresh()
                    } else {
                        Log.e("POSTS READ", "Prompt field is null")
                        setImageAvailable(false)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("POSTS READ", "Error getting documents", e)
            }

        imageRef.downloadUrl.addOnSuccessListener {
            Log.d("HomeView", "Image available at $it")
            setImageAvailable(true)
            capturedImageUri = it
        }.addOnFailureListener {
            Log.e("HomeView", "Image not available")
            setImageAvailable(false)
        }
        updatePostState()
    }

    fun loadPosts() {
        viewModelScope.launch {
            val db = Firebase.firestore

            // call to get posts
            db.collection("posts").get().addOnSuccessListener { querySnapshot ->
                val postsList = querySnapshot.map { document ->
                    PostItem("Prompt q here",
                        document.getString("caption"),
                        document.getString("date"),
                        document.getString("imageurl")
                    )
                }
                _posts.value = postsList
            }
        }
    }

    fun setImageAvailable(newValue: Boolean) {
        imageAvailable.value = newValue
    }

    fun setIsTaken(newValue: Boolean) {
        isTaken.value = newValue
    }
    fun deleteDocumentAndImage() {
        viewModelScope.launch {
            // Delete document from Firestore
            db.collection("posts").document(documentPath).delete().await()

            // Delete image from Firebase Storage
            val imageRef = storage.reference.child("images").child(documentPath)
            imageRef.delete().await()
            setImageAvailable(false)
            setIsTaken(false)
        }
    }

    fun updatePostState() {
        if (currentUser != null) {
            db.collection("posts").document("${currentUser.uid}_${today}.jpg")
                .get()
                .addOnSuccessListener { document ->
                    val fbCaption = document.getString("caption")
                    val fbPublic = document.getBoolean("public")
                    if (fbCaption !== null && fbPublic !== null) {
                        caption.value = fbCaption
                        public.value = fbPublic
                        isFetched.value = true
                    }

                }
                .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }
        }
    }

    fun updatePublicToggle(public: Boolean) {
        db.collection("posts").document(documentPath)
            .update("public", public)
            .addOnSuccessListener {
                Log.e("DOC PUBLIC", "Successfully updated public boolean")
            }
            .addOnFailureListener { e ->
                Log.e("ItemWithToggleAndButton", "Failed to update document", e)
            }
    }

    fun updateCaption(caption: String) {
        if (currentUser != null) {
            val postData = hashMapOf(
                "caption" to caption
            )
            db.collection("posts").document("${currentUser.uid}_${today}.jpg")
                .set(postData, SetOptions.merge())
                .addOnSuccessListener { Log.d("POSTS WRITE", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }
        }
    }

    fun cameraLauncher(isTaken: Boolean, context: Context, localUri: Uri) {
        if (isTaken && currentUser !== null) {
            val userUid = currentUser.uid
            Log.d("HomeView", "Image captured successfully")
            capturedImageUri = localUri
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
                            todayImageRef.downloadUrl.addOnSuccessListener {
                                Log.d("JEFFERY RAHHHHHHHHH", "Image available at $it")
                                capturedImageUri = it

                                val db = Firebase.firestore
                                val postData = hashMapOf(
                                    "public" to true,
                                    "date" to today,
                                    "time" to getCurrentTimeAsString(),
                                    "userid" to userUid,
                                    "caption" to "",
                                    "imageurl" to capturedImageUri,
                                    "prompt" to dailyPrompt.value
                                )

                                db.collection("posts").document("${userUid}_${today}.jpg")
                                    .set(postData)
                                    .addOnSuccessListener { Log.d("POSTS WRITE", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("POSTS WRITE", "Error writing document", e) }

                            }.addOnFailureListener {
                                Log.e("JEFFERY", "Image not available")
                            }
                            caption.value = ""
                            public.value = true
                            setImageAvailable(true)
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

    fun showPopupBeforeTakingPicture(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Posting images online makes them accessible to the public, so exercise discretion and consider privacy implications.")
        builder.setPositiveButton("I Understand") { dialog, _ ->
            // If the user clicks "Yes", call the camera launcher
            setIsTaken(true)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // If the user clicks "Cancel", dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun getCurrentTimeAsString(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss") // Define the format you want
        return currentTime.format(formatter)
    }
}