package com.example.dishpedia.api

import com.example.dishpedia.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    /**
     * retrofit builder to build the retrofit object
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RecipesApi by lazy {
        retrofit.create(RecipesApi::class.java)
    }
}