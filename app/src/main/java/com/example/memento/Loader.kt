package com.example.memento

import android.icu.text.SimpleDateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import org.example.userinterface.Equipment.PostItem
import java.util.Date

interface Loader<T> { suspend fun loadPosts(db: FirebaseFirestore): T }

class PostLoader : Loader<List<PostItem>> {
    override suspend fun loadPosts(db: FirebaseFirestore): List<PostItem> {

        var posts = MutableStateFlow<List<PostItem>>(emptyList())
        db.collection("posts").get().addOnSuccessListener { querySnapshot ->
            val postsList = querySnapshot.map { document ->
                PostItem("Prompt q here",
                    document.getString("caption"),
                    document.getString("date"),
                    document.getString("imageurl")
                )
            }
            posts.value = postsList
        }
        return posts.value
    }
}

class DatePostLoader : Loader<List<org.example.userinterface.History.PostItem>> {

    override suspend fun loadPosts(db: FirebaseFirestore): List<org.example.userinterface.History.PostItem> {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        var posts = MutableStateFlow<List<org.example.userinterface.History.PostItem>>(emptyList())
        // call to get posts
        db.collection("posts").orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener { querySnapshot ->
            val postsList = querySnapshot.map { document ->
                org.example.userinterface.History.PostItem(
                    document.getString("prompt"),
                    document.getString("caption"),
                    document.getString("date"),
                    document.getString("imageurl"),
                    document.getString("userid")
                )
            }
            posts.value = postsList.filter{it.userid == "${currentUser?.uid}"}
        }.await()
        return posts.value
    }
}

class TimePostLoader : Loader<List<PostItem>> {
    override suspend fun loadPosts(db: FirebaseFirestore): List<PostItem> {
        var posts = MutableStateFlow<List<PostItem>>(emptyList())
        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())
        // call to get posts
        db.collection("posts").orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener { querySnapshot ->
            val postsList = querySnapshot.map { document ->
                PostItem("Prompt q here",
                    document.getString("caption"),
                    document.getString("date"),
                    document.getString("imageurl")
                )

            }
            posts.value = postsList.filter { it.date == today }
        }
        return posts.value
    }
}
