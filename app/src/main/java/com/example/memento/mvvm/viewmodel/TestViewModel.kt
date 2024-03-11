package com.example.memento.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.example.model.TestModel

class TestViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val testCollection = db.collection("test")

    private val _test = MutableLiveData<List<TestModel>>()
    val tests: LiveData<List<TestModel>>
        get() = _test

    private var testListener: ListenerRegistration? = null

    init {
        fetchTestData()
    }

    private fun fetchTestData() {
        testCollection.get()
            .addOnSuccessListener { documents ->
                val testList = mutableListOf<TestModel>()
                for (document in documents) {
                    val data = document.getString("data")
                    if (data != null) {
                        testList.add(TestModel(data))
                    }
                }
                _test.value = testList
                Log.d("Fetch Data", "Successful")
            }
            .addOnFailureListener { exception ->
                Log.w("Fetch Data", "Error getting documents: ", exception)
            }
    }
    fun addNewField(newField: String) {
        val newData = hashMapOf(
            "data" to newField
        )
        testCollection.add(newData)
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the listener when ViewModel is cleared
        testListener?.remove()
    }
}

