package com.example.memento.mvvm.viewmodel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

class DiscoverViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts: StateFlow<List<PostItem>> = _posts
    val prompt: MutableState<String> = mutableStateOf("Daily Prompt")
    val today = android.icu.text.SimpleDateFormat("yyyy-MM-dd").format(Date())

    init {
        loadPosts()
        loadPrompt()
    }

    val imageAvailable: MutableState<Boolean> = mutableStateOf(false)


    fun loadPrompt() {
        println(today)
        db.collection("prompts")
            .whereEqualTo("timestamp", today)
            .get()
            .addOnSuccessListener {
                documents ->
                for (doc in documents) {
                    prompt.value = doc.getString("prompt") ?: "Daily Prompt"
                }
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

}