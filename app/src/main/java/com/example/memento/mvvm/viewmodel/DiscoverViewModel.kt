package com.example.memento.mvvm.viewmodel
import android.icu.text.SimpleDateFormat
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

class DiscoverViewModel : ViewModel() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    //private val _posted = MutableState<Boolean>(true)

    val currentUser = firebaseAuth.currentUser
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
    val documentPath = "${currentUser?.uid}_${today}.jpg"

    val posts: StateFlow<List<PostItem>> = _posts
    val posted: MutableState<Boolean> = mutableStateOf(true)

    init {
        verifyPost()
        loadPosts()
    }

    val imageAvailable: MutableState<Boolean> = mutableStateOf(false)


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

    fun verifyPost () {
        db.collection("posts").document(documentPath).get()
            .addOnSuccessListener { document ->
                if(!document.exists()){
                    posted.value = false
                }
                else{
                    posted.value = true
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