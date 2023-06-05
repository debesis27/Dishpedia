package com.example.dishpedia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.repository.MyRecipeRepository
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import com.example.dishpedia.viewmodel.uiState.isValid
import com.example.dishpedia.viewmodel.uiState.toMyRecipe
import com.example.dishpedia.viewmodel.uiState.toMyRecipeUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a recipe from the database
 */
class MyRecipeEditViewModel(
    private val myRecipeRepository: MyRecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var recipeUiState by mutableStateOf(MyRecipeUiState())
        private set

    private val recipeId: Int = checkNotNull(savedStateHandle[NavigationItemsProvider.MyRecipeEdit.recipeIdArg])

    init {
        viewModelScope.launch {
            recipeUiState = myRecipeRepository.getMyRecipe(recipeId)
                .filterNotNull()
                .first()
                .toMyRecipeUiState(actionEnabled = true)
        }
    }

    fun updateUiState(newRecipeUiState: MyRecipeUiState){
        recipeUiState = newRecipeUiState.copy(actionEnabled = newRecipeUiState.isValid())
    }

    suspend fun updateRecipe(){
        if(recipeUiState.isValid()){
            myRecipeRepository.updateMyRecipe(recipeUiState.toMyRecipe())
        }
    }
}