package com.example.dishpedia.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dishpedia.models.NavigationDrawerItemsProvider
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.repository.MyRecipeRepository

/**
 * ViewModel to retrieve, update and delete a recipe from the database
 */
class MyRecipeDetailViewModel(
    private val myRecipeRepository: MyRecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: Int = checkNotNull(savedStateHandle[NavigationItemsProvider.MyRecipeDetails.recipeIdArg])


}