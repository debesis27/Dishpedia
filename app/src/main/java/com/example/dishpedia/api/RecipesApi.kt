package com.example.dishpedia.api

import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RecipesApi {
    //api call to get random recipes
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int
    ): Response<Recipes>

    //get searched recipes
    @GET("/recipes/complexSearch")
    suspend fun getSearchedRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<Recipes>

    //get individual recipe details
    @GET("/recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Response<Recipe>
}