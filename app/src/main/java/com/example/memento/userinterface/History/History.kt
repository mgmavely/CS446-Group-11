package org.example.userinterface.History
import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.memento.BuildConfig
import com.example.memento.R
import com.example.memento.mvvm.viewmodel.DiscoverViewModel
import com.example.memento.theme.MementoTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.example.userinterface.Equipment.ImageCollection
import org.example.userinterface.Home.createImageFile
import java.util.Date
import java.util.Objects

data class PostItem(
    val promptQuestion: String?,
    val promptAnswer: String?,
    val date: String?,
    val imageURL: String?
)

@Composable
fun PostDisplay(
    modifier: Modifier = Modifier,
    post: PostItem,
) {
    /**
     * Represents a single post in the scrolling column
     */


        // val image = painterResource(id = post.image)
        val promptQuestion = post.promptQuestion
        val promptAnswer = post.promptAnswer
        val promptDate = post.date
        val imageURL = post.imageURL

        Box(
            modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp)

        ) {

        Column(){
            Image(
                painter = rememberImagePainter(imageURL),
                contentDescription = "Today's Memento",
                modifier = Modifier
                    .fillMaxHeight().fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .height(250.dp)
                    .padding(10.dp)
            )
            if (promptDate != null && promptAnswer != null ) {


            AutoResizingText(
                text = promptDate, targetTextSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            AutoResizingText(
                text = "Q:" + promptQuestion, targetTextSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            AutoResizingText(
                text = "A: " + promptAnswer, targetTextSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            }


        }
    }

}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HistoryView() {

    val viewModel = DiscoverViewModel()

    // Firebase
    val db = Firebase.firestore

    var isDataReady by remember { mutableStateOf(false) }
    var postsLocal by remember { mutableStateOf<List<PostItem>>(emptyList()) }
    // call to get posts
    val postsFirebase = db.collection("posts")
    postsFirebase.get().addOnSuccessListener { querySnapshot ->
        for (document in querySnapshot) {
            val caption = document.getString("caption")
            val date = document.getString("date")
            val imageURL = document.getString("imageurl")

            postsLocal = postsLocal.toMutableList().apply { add((PostItem("Prompt q here", caption, date, imageURL))) }

        }
        isDataReady = true
    }


    if (isDataReady) {
        Log.e("text", "$postsLocal.size")

        MementoTheme {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "YOUR POST HISTORY" , textAlign = TextAlign.Center, fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )

                        },
                    )
                }) {
                LazyColumn(
                    // Column is lazy which enables scrolling
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(4) {

                        // add all items
                        PostDisplay(
                            Modifier,
                            postsLocal[it]
                        )

                    }
                }
            }
        }
    }
}


@Composable
fun AutoResizingText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    targetTextSize: TextUnit,
    maxLines: Int = Int.MAX_VALUE
) {
    /**
     * Creates a Text widget that sizes to the shape of the container depending on the
     * length of the prompt
     */

    // state of the text
    val textSize = remember {
        mutableStateOf(targetTextSize)
    }

    /* Checks if the text is overflowing and if it is, sizes it down until it is not */
    Text(
        modifier = modifier,
        text = text,
        color = color,
        textAlign = TextAlign.Center,
        fontSize = textSize.value,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = {textLayoutResult ->
            val lineIndex = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(lineIndex)) {
                textSize.value = textSize.value * 0.9f
            }
        },
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    )
}