package org.example.userinterface.Equipment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.memento.mvvm.viewmodel.DiscoverViewModel
import com.example.memento.theme.MementoTheme

data class PostItem(
    val promptQuestion: String?,
    val promptAnswer: String?,
    val date: String?,
    val imageURL: String?
)

@Composable
fun Prompt(viewModel: DiscoverViewModel) {
    /**
     * Represents the prompt that stays at the top of the scrolling screen
     */

    var promptText = viewModel.prompt.value /* hardcoded text for now */

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
                AutoResizingText(
                    modifier = Modifier.
                        padding(10.dp),
                    text = promptText,
                    color = MaterialTheme.colorScheme.onBackground,
                    targetTextSize = 30.sp
                )
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = modifier
                .background(Color.White)
                .padding(10.dp)
        ) {

            /*
                Image(
                    painter = rememberImagePainter(post.imageURL),
                    contentDescription = "post",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )*/

            Image(
                painter = rememberImagePainter(post.imageURL),
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .height(250.dp)
                    .padding(10.dp),
                contentDescription = "image"
            )

        }
        if (post.promptAnswer != null) {
            Caption(
                caption = post.promptAnswer
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DiscoverView(viewModel: DiscoverViewModel = DiscoverViewModel()) {
    /**
     * The user view of the discover page
     */
    val posts by viewModel.posts.collectAsState()
    Log.e("text", "$posts")

    if(!viewModel.posted.value){
        MementoTheme {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "MEMENTO",
                                textAlign = TextAlign.Center,
                                fontSize = 65.sp,
                                lineHeight = 33.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        },
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.2f)
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoResizingText(
                            modifier = Modifier.
                            padding(10.dp),
                            text = "Make a public post today to discover others!",
                            color = MaterialTheme.colorScheme.onBackground,
                            targetTextSize = 30.sp
                        )
                    }
                    Prompt(viewModel = viewModel)
                }
            }
        }
    }

    else {
        MementoTheme {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "MEMENTO",
                                textAlign = TextAlign.Center,
                                fontSize = 65.sp,
                                lineHeight = 33.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        },
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    // Prompt at the top of the screen that doesn't change
                    Box(
                        Modifier
                    ) {
                        Prompt()
                    }
                    LazyColumn(
                        // Column is lazy which enables scrolling
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp)
                            .background(MaterialTheme.colorScheme.primary),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(posts.size) { index ->
                            PostDisplay(Modifier, posts[index])
                        }
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

@Composable
fun Caption(caption: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val toggleExpanded: () -> Unit = { isExpanded = !isExpanded }

    Text(
        text = caption,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = toggleExpanded),
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
        overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
    )
}