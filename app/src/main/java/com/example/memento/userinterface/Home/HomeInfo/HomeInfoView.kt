package org.example.userinterface.Equipment.EquipmentInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.memento.mvvm.viewmodel.TestViewModel

@Composable
fun HomeInfoView() {
    val localViewModel = TestViewModel()
    val texts by localViewModel.tests.observeAsState(initial = emptyList())
    var newField by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = newField,
                onValueChange = { newField = it },
                label = { Text("Enter new field") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                localViewModel.addNewField(newField)
                newField = ""
            }) {
                Text("Submit")
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(texts) { text ->
                    Text(text = "Field: ${text.data}")
                }
            }
        }
    }
}

