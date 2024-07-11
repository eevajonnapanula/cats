package com.eevajonna.cats.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.eevajonna.cats.ui.utils.isEven

@Composable
fun CardStack(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content,
        modifier,
    ) { measurables, constraints ->

        val placeables =
            measurables.map { measurable ->
                measurable.measure(constraints)
            }

        val height =
            if (placeables.isNotEmpty()) {
                placeables.first().height +
                    (CardStack.EXTRA_PADDING * placeables.size)
            } else {
                0
            }

        val width =
            if (placeables.isNotEmpty()) {
                placeables.first().width
            } else {
                0
            }

        layout(width = width, height = height) {
            placeables.mapIndexed { index, placeable ->
                placeable.place(
                    x =
                        if (index.isEven()) {
                            0
                        } else {
                            CardStack.X_POSITION
                        },
                    y = CardStack.Y_POSITION * index,
                )
            }
        }
    }
}

object CardStack {
    const val EXTRA_PADDING = 10
    const val Y_POSITION = 5
    const val X_POSITION = 5
}
