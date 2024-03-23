package org.example.userinterface.History
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.memento.R
import com.example.memento.theme.MementoTheme
import com.google.firebase.auth.FirebaseAuth
import org.example.userinterface.Equipment.ImageCollection

data class PostItem(
    val promptQuestion: String,
    val promptAnswer: String,
    val date: String,
    val image: Int
)
class PostCollection() {
    /**
     *  Represents the View's internal collection of image objects
     */

    val posts: MutableList<PostItem> = mutableListOf()
    init {
        /* hardcoded images for now */
        posts.add(PostItem("What's your favourite dog", "Pug", "March 14 2024",  R.drawable.dog))
        posts.add(PostItem("What's your favorite kind of fire", "Campfire", "March 11 2024",  R.drawable.campfire))
        posts.add(PostItem("What's your favourite flower", "Pink one", "March 10 2024",  R.drawable.flowers))
    }


    fun getPainter(index: Int): PostItem {
        return if (index in 0 until posts.size) {
            posts[index]
        } else {
            PostItem("","","", -1)
        }
    }
}
@Composable
fun PostDisplay(
    modifier: Modifier = Modifier,
    post: PostItem
) {
    /**
     * Represents a single post in the scrolling column
     */

    if (post.image != -1) {
        val image = painterResource(id = post.image)
        val promptQuestion = post.promptQuestion
        val promptAnswer = post.promptAnswer
        val promptDate = post.date

        Box(
            modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp)

        ) {

        Column(){

            Image(
                painter = image,
                contentDescription = "post",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .height(250.dp)
                    .padding(10.dp)
            )

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
    val images = PostCollection()

    MementoTheme {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "YOUR POST HISTORY", textAlign = TextAlign.Center, fontSize = 30.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                    },
                )
            }) {

            LazyColumn(
                // Column is lazy which enables scrolling
                modifier = Modifier
                    .padding(top = 80.dp, bottom = 80.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.spacedBy(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(5) {
                    // add all items
                    PostDisplay(
                        Modifier,
                        images.getPainter(it)
                    )

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