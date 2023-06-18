package com.example.dishpedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dishpedia.utils.RecipeList
import com.example.dishpedia.viewmodel.RecipesUiState
import com.example.dishpedia.viewmodel.RecipesViewModel

@Composable
fun SearchScreen(
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    val recipeUiState = recipesViewModel.searchedRecipesUiState
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            SearchBar(recipesViewModel)
            when(recipeUiState){
                is RecipesUiState.Success -> RecipeList(recipeUiState.recipes, recipesViewModel, navController)
                is RecipesUiState.Loading -> { /* Don't do anything */ }
                is RecipesUiState.Error -> ErrorScreen()
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
                recipesViewModel.getSearchedRecipes(query)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(50.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
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
