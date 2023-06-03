package com.example.dishpedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishpedia.data.MyRecipe
import com.example.dishpedia.repository.MyRecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val TIMEOUT_MILLIS = 5_000L

data class MyRecipeListUiState(val myRecipeList: List<MyRecipe> = listOf())

class MyRecipeListViewModel(private val myRecipeRepository: MyRecipeRepository) : ViewModel(){
    val myRecipeListUiState : StateFlow<MyRecipeListUiState> =
        myRecipeRepository.getAllMyRecipe().map { MyRecipeListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MyRecipeListUiState()
            )

}