package com.example.dishpedia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.dishpedia.DishpediaApplication
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.repository.RecipeRepository
import com.example.dishpedia.utils.Constants.Companion.API_KEY
import com.example.dishpedia.utils.Constants.Companion.QUERY
import com.example.dishpedia.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.dishpedia.utils.Constants.Companion.QUERY_API_KEY
import com.example.dishpedia.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.dishpedia.utils.Constants.Companion.QUERY_INSTRUCTIONS
import com.example.dishpedia.utils.Constants.Companion.QUERY_NUMBER
import com.example.dishpedia.utils.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface RecipeUiState{
    data class Success(val recipes: Recipes) : RecipeUiState
    object Error : RecipeUiState
    object Loading : RecipeUiState
}

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {
    /**
     * The mutable State that stores the status of the most recent request
     */
    var randomRecipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)
    var searchedRecipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)

    /**
     * Call getRandomRecipes() on init so we can display status immediately.
     */
    init {
        getRandomRecipes(API_KEY, 20)
    }

    private fun getRandomRecipes(apiKey: String, number: Int){
        viewModelScope.launch {
            randomRecipeUiState = try {
                RecipeUiState.Success(repository.getRandomRecipes(apiKey, number))
            }catch (e: IOException){
                RecipeUiState.Error
            }catch (e: HttpException){
                RecipeUiState.Error
            }
        }
    }

    fun getSearchedRecipes(searchTerm: String){
        val query = applySearchQueries(searchTerm)
        viewModelScope.launch {
           searchedRecipeUiState = try {
               RecipeUiState.Success(repository.getSearchedRecipes(query))
           }catch (e: IOException){
               RecipeUiState.Error
           }catch (e: HttpException){
               RecipeUiState.Error
           }
        }
    }

//    fun getRecipeById(id: Int, apiKey: String){
//        viewModelScope.launch {
//            val response = repository.getRecipeById(id, apiKey)
//            individualRecipe.value = response
//        }
//    }

    fun applyCategoryQueries(courseType: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "10"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = courseType
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQueries(searchTerm: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY] = searchTerm
        queries[QUERY_NUMBER] = "10"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DishpediaApplication)
                val recipeRepository = application.container.recipeRepository
                RecipesViewModel(recipeRepository)
            }
        }
    }
}