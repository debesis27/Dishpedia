package com.example.dishpedia.repository

import com.example.dishpedia.api.RetrofitInstance
import com.example.dishpedia.models.*
import retrofit2.Response

/**
 * Repository for all methods
 */
class RecipeRepository {
    suspend fun getRandomRecipes(apiKey: String, number: Int): Response<Recipes> {
        return RetrofitInstance.api.getRandomRecipes(apiKey, number)
    }

    suspend fun getSearchedRecipes(query: Map<String, String>): Response<Recipes> {
        return RetrofitInstance.api.getSearchedRecipes(query)
    }

    suspend fun getRecipeById(id: Int, apiKey: String): Response<Recipe> {
        return RetrofitInstance.api.getRecipeById(id, apiKey)
    }
}