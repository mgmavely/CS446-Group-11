package com.example.memento.mvvm.viewmodel
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
import org.example.userinterface.History.PostItem

data class PostItem(
    val promptQuestion: String?,
    val promptAnswer: String?,
    val date: String?,
    val imageURL: String?
)
class HistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts: StateFlow<List<PostItem>> = _posts

    init {
        loadPosts()
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