package com.example.dishpedia

import com.example.dishpedia.api.RecipesApiService
import com.example.dishpedia.repository.RecipeRepository
import com.example.dishpedia.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val recipeRepository: RecipeRepository
}

class DefaultAppContainer : AppContainer {
    /**
     * retrofit builder to build the retrofit object
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: RecipesApiService by lazy {
        retrofit.create(RecipesApiService::class.java)
    }

    override val recipeRepository: RecipeRepository by lazy {
        RecipeRepository(api)
    }
}