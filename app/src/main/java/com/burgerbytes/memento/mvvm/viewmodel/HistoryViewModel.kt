package com.burgerbytes.memento.mvvm.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burgerbytes.memento.DatePostLoader
import com.burgerbytes.memento.Loader
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
import org.burgerbytes.userinterface.History.PostItem

class HistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    private val _posts = MutableStateFlow<List<PostItem>>(emptyList())
    val posts: StateFlow<List<PostItem>> = _posts

    val loader: Loader<List<PostItem>> = DatePostLoader()

    init {
        viewModelScope.launch {
            _posts.value = loader.loadPosts(Firebase.firestore)
        }
//         loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            val db = Firebase.firestore

            // call to get posts
            db.collection("posts").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener { querySnapshot ->
                val postsList = querySnapshot.map { document ->
                    PostItem(document.getString("prompt"),
                        document.getString("caption"),
                        document.getString("date"),
                        document.getString("imageurl"),
                        document.getString("userid")
                    )
                }
                _posts.value = postsList.filter{it.userid == "${currentUser?.uid}"}
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