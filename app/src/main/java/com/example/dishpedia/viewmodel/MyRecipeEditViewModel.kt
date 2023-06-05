package com.example.dishpedia.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.dishpedia.repository.MyRecipeRepository

class MyRecipeEditViewModel(
    private val myRecipeRepository: MyRecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

}