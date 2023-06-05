package com.example.dishpedia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dishpedia.repository.MyRecipeRepository
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import com.example.dishpedia.viewmodel.uiState.isValid
import com.example.dishpedia.viewmodel.uiState.toMyRecipe

/**
 * ViewModel to validate and insert recipes in database
 */
class MyRecipeEntryViewModel(private val myRecipeRepository: MyRecipeRepository) : ViewModel(){
    var recipeUiState by mutableStateOf(MyRecipeUiState())
        private set

    fun updateUiState(newRecipeUiState: MyRecipeUiState){
        recipeUiState = newRecipeUiState.copy(actionEnabled = newRecipeUiState.isValid())
    }

    suspend fun saveRecipe(){
        if(recipeUiState.isValid()){
            myRecipeRepository.insertMyRecipe(recipeUiState.toMyRecipe())
        }
    }
}