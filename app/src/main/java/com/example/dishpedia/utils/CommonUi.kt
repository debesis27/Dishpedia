package com.example.dishpedia.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationItem
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.ui.theme.Purple500
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable that shows a list of recipes using LazyColumn
 */
@Composable
fun RecipeList(
    recipes: Recipes,
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    LazyColumn{
        items(recipes.recipes){recipe ->
            RecipeCard(recipe, recipesViewModel, navController)
        }
    }
}

/**
 * Composable that gives a short info of the recipe. It is used by [RecipeList]
 */
@Composable
private fun RecipeCard(
    recipe: Recipe,
    recipesViewModel: RecipesViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                recipesViewModel.getRecipeById(recipe.id)
                navController.navigate(NavigationItemsProvider.recipeInfo.route)
            }
        ,
        elevation = 4.dp
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                modifier = Modifier.fillMaxWidth(),
                error = painterResource(id = R.drawable.ic_connection_error),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = recipe.title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

/**
 * Composable that shows the Navigation drawer
 */
@Composable
fun NavigationDrawer(
    navItems: List<NavigationItem>,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(126.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.guest_icon),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        navItems.forEach{ item ->
            val selected = item.route == backStackEntry?.destination?.route

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate(item.route)
                    },
                backgroundColor = if (selected) Purple500 else Color.White,
                elevation = 0.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Composable for creating or updating a recipe in the database
 */
@Composable
fun MyRecipeEditBody(
    myRecipeUiState: MyRecipeUiState,
    onRecipeValueChange: (MyRecipeUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        MyRecipeInputForm(
            myRecipeUiState = myRecipeUiState,
            onRecipeValueChange = onRecipeValueChange
        )
        Button(
            onClick = onSaveClick,
            enabled = myRecipeUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.save_button))
        }
    }
}

/**
 * Composable consisting of all text fields in [MyRecipeEditBody]
 */
@Composable
fun MyRecipeInputForm(
    myRecipeUiState: MyRecipeUiState,
    onRecipeValueChange: (MyRecipeUiState) -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //TODO: Add a method to get image

        OutlinedTextField(
            value = myRecipeUiState.title,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(title = it)) },
            label = { Text(stringResource(id = R.string.recipe_title_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = myRecipeUiState.summary,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(summary = it)) },
            label = { Text(stringResource(id = R.string.recipe_summary_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = myRecipeUiState.readyInMinutes,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(readyInMinutes = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_ready_in_minutes_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = myRecipeUiState.servings,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(servings = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_servings_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        //TODO: Add an OutlinedTextField with trailing icon of add, then add more OutlinedTextField depending on it
    }
}
