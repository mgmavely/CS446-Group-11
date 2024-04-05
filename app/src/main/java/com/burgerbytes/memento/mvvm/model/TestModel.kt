package org.burgerbytes.model

import com.google.firebase.firestore.DocumentSnapshot

data class TestModel(
     val data: String = ""
 ) {
     companion object {
         fun fromSnapshot(snapshot: DocumentSnapshot): TestModel {
             val data = snapshot.data ?: throw IllegalArgumentException("DocumentSnapshot is null")
             return TestModel(
                 data = data["data"] as? String ?: ""
             )
         }
     }
 }