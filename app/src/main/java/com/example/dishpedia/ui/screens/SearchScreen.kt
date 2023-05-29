package com.example.dishpedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel

@Composable
fun SearchScreen(recipesViewModel: RecipesViewModel){
    val recipeUiState = recipesViewModel.searchedRecipeUiState
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            SearchBar(recipesViewModel)
            when(recipeUiState){
                is RecipeUiState.Success -> RecipeList(recipes = recipeUiState.recipes)
            }
        }
    }
}

@Composable
fun SearchBar(recipesViewModel: RecipesViewModel){
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        placeholder = { Text(text = "Search") },
        onValueChange = { onQueryChanged ->
            query = onQueryChanged
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                tint = MaterialTheme.colors.onBackground,
                contentDescription = "Search icon",
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = MaterialTheme.colors.onBackground,
                        contentDescription = "Clear icon"
                    )
                }
            }
        },
        maxLines = 1,
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                //TODO: Add search
                recipesViewModel.getSearchedRecipes(query)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(50.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
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

@Composable
private fun RecipeList(
    recipes: Recipes
){
    LazyColumn{
        items(recipes.recipes){recipe ->
            RecipeCard(recipe = recipe)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SearchBarPreview(){
//    Surface(modifier = Modifier.fillMaxSize()) {
//        SearchBar()
//    }
//}