package com.example.dishpedia.utils

class Constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com/"
        const val API_KEY = "94bb13a023524dc9aa8dd13c48e00c15" //TODO: Add your Api Key here

        //API query keys
        const val QUERY = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_INSTRUCTIONS = "instructionsRequired"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_INCLUDE_NUTRITION = "includeNutrition"

        const val TIMEOUT_MILLIS = 5_000L
    }
}