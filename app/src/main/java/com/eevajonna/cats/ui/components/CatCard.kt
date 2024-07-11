package com.eevajonna.cats.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.eevajonna.cats.R
import com.eevajonna.cats.ui.theme.CatsTheme

@Composable
fun CatCard(
    id: String,
    modifier: Modifier = Modifier,
    removeCat: () -> Unit,
) {
    ElevatedCard(
        modifier =
            modifier
                .padding(CatCard.padding)
                .size(CatCard.size),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = CatCard.elevation,
            ),
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                removeCat()
            },
        ) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = stringResource(R.string.icon_button_delete),
            )
        }
        SubcomposeAsyncImage(
            model = "${CatCard.url}$id",
            modifier =
                Modifier
                    .padding(CatCard.padding)
                    .clip(CatCard.cardShape)
                    .align(Alignment.CenterHorizontally),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                CircularProgressIndicator()
            } else {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun AnimatedCatCard(
    id: String,
    modifier: Modifier = Modifier,
    isEven: Boolean = true,
    removeCat: () -> Unit,
) {
    var visible by remember(id) {
        mutableStateOf(true)
    }

    AnimatedVisibility(
        visible = visible,
        exit =
            slideOut { fullSize ->
                val xOffset = if (isEven) -fullSize.width else fullSize.width
                IntOffset(
                    xOffset,
                    -(fullSize.height / 8),
                )
            },
    ) {
        CatCard(
            id = id,
            modifier = modifier,
        ) {
            visible = false
            removeCat()
        }
    }
}

object CatCard {
    val cardShape = RoundedCornerShape(12.dp)
    val padding = 12.dp
    val size = 350.dp
    val elevation = 6.dp
    val url = "https://cataas.com/cat/"
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CatCardPreview() {
    CatsTheme {
        CatCard(id = "5MZcPeMXyk4IAUQx") {}
    }
}
