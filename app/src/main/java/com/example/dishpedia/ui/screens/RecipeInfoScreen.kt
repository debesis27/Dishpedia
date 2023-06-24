package com.example.dishpedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dishpedia.R
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.utils.RecipeInfo
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.example.dishpedia.viewmodel.TabsViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun RecipeInfoScreen(
    recipeViewModel: RecipesViewModel,
    navigateUp: () -> Unit,
    tabsViewModel: TabsViewModel = viewModel()
){
    when(val recipeUiState = recipeViewModel.recipeUiState){
        is RecipeUiState.Loading -> LoadingScreen()
        is RecipeUiState.Error -> ErrorScreen()
        is RecipeUiState.Success -> RecipeDetailsScreen(
            recipe = recipeUiState.recipe,
            tabsViewModel = tabsViewModel,
            navigateUp = navigateUp
        )
    }
}

@Composable
private fun RecipeDetailsScreen(
    recipe: Recipe,
    tabsViewModel: TabsViewModel,
    navigateUp: () -> Unit
){
    val ingredients = mutableListOf<String>()
    var index = 0
    recipe.extendedIngredients.forEach { ingredient ->
        ingredients.add(index, ingredient.original)
        index += 1
    }

    val instructions = mutableListOf<String>()
    index = 0
    recipe.analyzedInstructions.forEach { item ->
        item.steps.forEach { instruction ->
            instructions.add(index, instruction.step)
            index += 1
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ){
        RecipeInfo(
            image = recipe.image,
            title = recipe.title,
            category = if(recipe.cuisines.isNotEmpty()) recipe.cuisines.component1() else "NA",
            cookTime = recipe.readyInMinutes.toString(),
            vegetarian = recipe.vegetarian,
            summary = recipe.summary,
            servings = recipe.servings.toString(),
            ingredients = ingredients,
            instructions = instructions,
            nutrients = recipe.nutrition.nutrients,
            tabsViewModel = tabsViewModel
        )

        RecipeInfoScreenAppBar(navigateUp = navigateUp)
    }
}

@Composable
private fun RecipeInfoScreenAppBar(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(text = "")},
        modifier = modifier,
        backgroundColor = Color.Transparent,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = Color.Black
                )
            }
        },
        elevation = 0.dp
    )
}

@Composable
private fun ErrorScreen(){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(
            text = "Sorry, Please try again later",
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
private fun LoadingScreen(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .placeholder(
                    visible = true,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.fade()
                ),
            shape = MaterialTheme.shapes.large,
            elevation = 0.dp
        ) {}

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(250.dp))

            Card(
                modifier = Modifier
                    .width(350.dp)
                    .height(150.dp)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp),
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                elevation = 8.dp
            ) {

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(420.dp))
            Box(
               modifier = Modifier
                   .height(20.dp)
                   .width(350.dp)
                   .placeholder(
                       visible = true,
                       color = Color.Gray,
                       shape = RoundedCornerShape(4.dp),
                       highlight = PlaceholderHighlight.fade()
                   )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(350.dp)
                    .placeholder(
                        visible = true,
                        color = Color.Gray,
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.fade()
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview(){
    LoadingScreen()
}
