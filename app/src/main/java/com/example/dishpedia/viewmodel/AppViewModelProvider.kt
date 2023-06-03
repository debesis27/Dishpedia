package com.example.dishpedia.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dishpedia.DishpediaApplication

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        //Initializer for RecipesViewModel
        initializer {
            RecipesViewModel(dishpediaApplication().container.recipeRepository)
        }

        //Initializer for MyRecipeListViewModel
        initializer {
            MyRecipeListViewModel(dishpediaApplication().container.myRecipeRepository)
        }

        //Initializer for MyRecipeDetailViewModel
        initializer {
            MyRecipeDetailViewModel(
                dishpediaApplication().container.myRecipeRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [DishpediaApplication].
 */
fun CreationExtras.dishpediaApplication(): DishpediaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DishpediaApplication)
