package com.example.dishpedia.repository

import com.example.dishpedia.AppContainer
import com.example.dishpedia.api.RecipesApiService
import com.example.dishpedia.models.*

/**
 * Repository for all methods
 */
class RecipeRepository(private val recipeApiService: RecipesApiService) {
    suspend fun getRandomRecipes(apiKey: String, number: Int): Recipes {
        return recipeApiService.getRandomRecipes(apiKey, number)
    }

    suspend fun getSearchedRecipes(query: Map<String, String>): Recipes {
        return recipeApiService.getSearchedRecipes(query)
    }

    suspend fun getRecipeById(id: Int, apiKey: String): Recipe {
        return recipeApiService.getRecipeById(id, apiKey)
    }
}