package org.example.userinterface.Equipment.EquipmentInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.model.TestViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue

@Composable
fun HomeInfoView() {
    val localViewModel = TestViewModel()
    val texts by localViewModel.tests.observeAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        LazyColumn {
            items(texts) { text ->
                // For each user, display a Text composable
                Text(text = "Field: ${text.data}")
            }
        }

    }
}

//TODO: Drag refresh function
