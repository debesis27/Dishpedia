package com.example.dishpedia.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.repository.MyRecipeRepository
import com.example.dishpedia.utils.Constants.Companion.TIMEOUT_MILLIS
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import com.example.dishpedia.viewmodel.uiState.toMyRecipe
import com.example.dishpedia.viewmodel.uiState.toMyRecipeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve, update and delete a recipe from the database
 */
class MyRecipeDetailViewModel(
    private val myRecipeRepository: MyRecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: Int = checkNotNull(savedStateHandle[NavigationItemsProvider.MyRecipeDetails.recipeIdArg])

    val uiState: StateFlow<MyRecipeUiState> =
        myRecipeRepository.getMyRecipe(recipeId)
            .filterNotNull()
            .map {
                it.toMyRecipeUiState()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyRecipeUiState()
            )

    suspend fun deleteMyRecipe(){
        myRecipeRepository.deleteMyRecipe(uiState.value.toMyRecipe())
    }
}