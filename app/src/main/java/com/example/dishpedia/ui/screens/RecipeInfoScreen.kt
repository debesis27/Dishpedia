package com.example.dishpedia.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat.fromHtml
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.utils.ErrorScreen
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.example.dishpedia.viewmodel.TabsViewModel
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import de.charlex.compose.HtmlText

@Composable
fun RecipeInfoScreen(
    recipeViewModel: RecipesViewModel,
    navigateUp: () -> Unit,
    tabsViewModel: TabsViewModel = viewModel()
){
    val tabIndex = tabsViewModel.tabIndex.observeAsState()
    val recipeUiState = recipeViewModel.recipeUiState

    Scaffold(
        topBar = {
            RecipeInfoScreenAppBar(
                title = when(recipeUiState){
                    is RecipeUiState.Success -> recipeUiState.recipe.title
                    else -> ""
                },
                navigateUp = navigateUp,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            TabRow(
                selectedTabIndex = tabIndex.value!!,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                tabsViewModel.tabs.forEachIndexed{ index, title ->
                    Tab(
                        selected = tabIndex.value!! == index,
                        modifier = Modifier.padding(8.dp),
                        onClick = { tabsViewModel.updateTabIndex(index) }
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h3
                        )
                    }
                }
            }

            when(recipeUiState){
                is RecipeUiState.Success -> when(tabIndex.value){
                    0 -> SummaryScreen(recipeUiState.recipe, tabsViewModel)
                    1 -> IngredientsScreen(recipeUiState.recipe, tabsViewModel)
                    2 -> InstructionsScreen(recipeUiState.recipe, tabsViewModel)
                }

                else -> ErrorScreen()
            }
        }
    }


}

@Composable
fun RecipeInfoScreenAppBar(
    title: String,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(
            text = title,
            style = MaterialTheme.typography.h2
        )},
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        },
        elevation = 0.dp
    )
}


@Composable
private fun SummaryScreen(
    recipe: Recipe,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = MaterialTheme.shapes.large,
            elevation = 0.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillWidth
            )
        }
        Text(text = recipe.title)
        Text(text = stringResource(id = R.string.cook_time))
        Text(text = recipe.readyInMinutes.toString())
        Text(text = stringResource(id = R.string.servings))
        Text(text = recipe.servings.toString())
        Text(text = stringResource(id = R.string.description))
        HtmlText(text = recipe.summary)
    }
}

@Composable
private fun IngredientsScreen(
    recipe: Recipe,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        recipe.extendedIngredients.forEach{ item ->
            Text(text = item.original)
        }
    }
}

@Composable
private fun InstructionsScreen(
    recipe: Recipe,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ){
        recipe.analyzedInstructions.forEach{ items ->
            items.steps.forEach{ item ->
                Row {
                    Text(text = item.number.toString())
                    Text(text = item.step)
                }
            }
        }
    }
}
