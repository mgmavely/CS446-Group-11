package org.example.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

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
        testListener = testCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle any errors
                // For example, you can log the error or show a toast message
                return@addSnapshotListener
            }

            // Parse the snapshot to get the list of users
            val testList = mutableListOf<TestModel>()
            for (doc in snapshot!!) {
                val value = TestModel.fromSnapshot(doc)
                testList.add(value)
            }

            // Update the LiveData with the new list of users
            _test.value = testList

            // Remove the listener after fetching initial data
            testListener?.remove()
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the listener when ViewModel is cleared
        testListener?.remove()
    }
}

