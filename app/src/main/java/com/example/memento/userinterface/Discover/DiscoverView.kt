package org.example.userinterface.Equipment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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


class ImageCollection() {
    /**
     *  Represents the View's internal collection of image objects
     */

    val painters: MutableList<Int> = mutableListOf()
    init {
        /* hardcoded images for now */
        painters.add(R.drawable.dog)
        painters.add(R.drawable.campfire)
        painters.add(R.drawable.flowers)
        painters.add(R.drawable.forest)
        painters.add(R.drawable.ocean)
    }

    fun getAll(): MutableList<Int> {
        return painters
    }

    fun getPainter(index: Int): Int {
        return if (index in 0 until painters.size) {
            painters[index]
        } else {
            -1
        }
    }
}

@Composable
fun Prompt() {
    /**
     * Represents the prompt that stays at the top of the scrolling screen
     */

    var promptText = "Take a picture of something beautiful..." /* hardcoded text for now */

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
                AutoResizingText(
                    modifier = Modifier.
                        padding(10.dp),
                    text = promptText,
                    color = MaterialTheme.colorScheme.primary,
                    targetTextSize = 30.sp
                )
    }
}

@Composable
fun Post(
    modifier: Modifier = Modifier,
    imagePath: Int
) {
    /**
     * Represents a single post in the scrolling column
     */

    val image = painterResource(id = imagePath)

    Box(
        modifier = modifier
            .size(380.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(10.dp)
    ) {
        Image(
            painter = image,
            contentDescription = "post",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
    }

}

@Preview
@Composable
fun DiscoverView() {
    /**
     * The user view of the discover page
     */

    MementoTheme {
        val images = ImageCollection()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Prompt at the top of the screen that doesn't change
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
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
                items(5) {
                    // add all items
                    Post(
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