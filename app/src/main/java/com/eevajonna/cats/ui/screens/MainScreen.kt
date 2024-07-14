package com.eevajonna.cats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eevajonna.cats.CatsViewModel
import com.eevajonna.cats.R
import com.eevajonna.cats.ui.components.AnimatedCatCard
import com.eevajonna.cats.ui.components.CardStack
import com.eevajonna.cats.ui.components.CatCard
import com.eevajonna.cats.ui.utils.isEven
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MainScreen(
    viewModel: CatsViewModel,
    modifier: Modifier = Modifier,
) {
    val catIds = viewModel.cats.collectAsState()
    val selectedCat = viewModel.selectedCat.collectAsState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    fun deleteCat(
        id: String,
        focusToPrevious: Boolean = false,
    ) {
        scope.launch {
            viewModel.deleteCat(id)
            delay(1000)
            if (focusToPrevious) focusManager.moveFocus(FocusDirection.Previous)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    horizontal = MainScreen.horizontalPadding,
                    vertical = MainScreen.verticalPadding,
                )
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MainScreen.verticalSpacing),
    ) {
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.titleLarge,
        )

        selectedCat.value?.let { id ->
            CatCard(id = id, lastIdOnStack = id) {
                deleteCat(id)
            }
        }

        Button(
            modifier = Modifier.padding(MainScreen.buttonPadding),
            onClick = {
                viewModel.getNewCat()
            },
        ) {
            val text =
                if (selectedCat.value == null) {
                    stringResource(R.string.get_a_cat)
                } else {
                    stringResource(
                        R.string.get_another_cat,
                    )
                }
            Text(text)
        }

        if (catIds.value.isNotEmpty()) {
            Text(
                text = stringResource(R.string.all_the_other_cats),
                style = MaterialTheme.typography.titleLarge,
            )

            CardStack {
                val lastItem = catIds.value.last()

                catIds.value.mapIndexed { index, id ->
                    val degrees =
                        remember {
                            Random.nextInt(-2, 2).toFloat()
                        }

                    AnimatedCatCard(
                        modifier = Modifier.rotate(degrees),
                        isEven = index.isEven(),
                        id = id,
                        lastIdOnStack = lastItem,
                    ) {
                        deleteCat(
                            id = id,
                            focusToPrevious = true,
                        )
                    }
                }
            }
        }
    }
}

object MainScreen {
    val horizontalPadding = 16.dp
    val verticalPadding = 24.dp
    val verticalSpacing = 12.dp
    val buttonPadding = 12.dp
}
