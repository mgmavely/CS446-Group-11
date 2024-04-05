package com.example.memento.mvvm.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memento.Loader
import com.example.memento.DiscoverPostLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    val currentUser = firebaseAuth.currentUser
    val today = SimpleDateFormat("yyyy-MM-dd").format(Date())

    val posts: StateFlow<List<PostItem>> = _posts
    val posted: MutableState<Boolean> = mutableStateOf(false)
    val prompt: MutableState<String> = mutableStateOf("Daily Prompt")

    val loader: Loader<List<PostItem>> = DiscoverPostLoader()

    init {
        verifyPost()
        viewModelScope.launch {
            _posts.value = loader.loadPosts(Firebase.firestore)
        }
//        loadPosts()
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
            db.collection("posts").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener { querySnapshot ->
                val postsList = querySnapshot.map { document ->
                    PostItem("Prompt q here",
                        document.getString("caption"),
                        document.getString("date"),
                        document.getString("imageurl")
                    )

                }
                _posts.value = postsList.filter { it.date == today }
            }


        }

    }

    fun verifyPost () {
        db.collection("posts")
            .whereEqualTo("public", true)
            .whereEqualTo("userid", "${currentUser?.uid}")
            .whereEqualTo("date", "${today}").get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty()){
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