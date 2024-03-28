package com.example.memento.mvvm.viewmodel
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.example.userinterface.Equipment.PostItem
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
    var dailyPrompt = ""
    var capturedImageUri: Uri? = null

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
            setImageAvailable(true)
            capturedImageUri = it
        }.addOnFailureListener {
            Log.e("HomeView", "Image not available")
            setImageAvailable(false)
        }
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
    fun deleteDocumentAndImage(deleteKey: String) {
        viewModelScope.launch {
            // Delete document from Firestore
            db.collection("posts").document(deleteKey).delete().await()

            // Delete image from Firebase Storage
            val imageRef = storage.reference.child("images").child(deleteKey)
            imageRef.delete().await()
        }
    }

    fun updatePostState(): Triple<String, Boolean, Boolean> {
        var caption = ""
        var public = false
        var isFetched = false
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
        return Triple(caption, public, isFetched)
    }

}