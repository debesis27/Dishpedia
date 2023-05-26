package com.example.dishpedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.repository.RecipeRepository
import com.example.dishpedia.utils.Constants.Companion.API_KEY
import com.example.dishpedia.utils.Constants.Companion.QUERY
import com.example.dishpedia.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.dishpedia.utils.Constants.Companion.QUERY_API_KEY
import com.example.dishpedia.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.dishpedia.utils.Constants.Companion.QUERY_INSTRUCTIONS
import com.example.dishpedia.utils.Constants.Companion.QUERY_NUMBER
import kotlinx.coroutines.launch
import retrofit2.Response

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {
    var recipesResponse: MutableLiveData<Response<Recipes>> = MutableLiveData()
    var individualRecipe: MutableLiveData<Response<Recipe>> = MutableLiveData()

    fun getRandomRecipes(apiKey: String, number: Int){
        viewModelScope.launch {
            val response = repository.getRandomRecipes(apiKey, number)
            recipesResponse.value = response
        }
    }

    fun getSearchedRecipes(query: HashMap<String, String>){
        viewModelScope.launch {
            val response = repository.getSearchedRecipes(query)
            recipesResponse.value = response
        }
    }

    fun getRecipeById(id: Int, apiKey: String){
        viewModelScope.launch {
            val response = repository.getRecipeById(id, apiKey)
            individualRecipe.value = response
        }
    }

    fun applySearchQueries(searchTerm: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY] = searchTerm
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}