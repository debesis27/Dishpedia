package com.example.dishpedia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dishpedia.models.CategoryListItems
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
import com.example.dishpedia.utils.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface RecipesUiState{
    data class Success(val recipes: Recipes) : RecipesUiState
    object Error : RecipesUiState
    object Loading : RecipesUiState
}

sealed interface RecipeUiState{
    data class Success(val recipe: Recipe) : RecipeUiState
    object Error : RecipeUiState
    object Loading : RecipeUiState
}

sealed interface CategoryRecipesUiState{
    data class Success(val recipes: Recipes, val category: CategoryListItems) : CategoryRecipesUiState
    object Error : CategoryRecipesUiState
    object Loading : CategoryRecipesUiState
}

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {
    /**
     * The mutable States that stores the status of the most recent request
     */
    var randomRecipesUiState: RecipesUiState by mutableStateOf(RecipesUiState.Loading)
    var searchedRecipesUiState: RecipesUiState by mutableStateOf(RecipesUiState.Loading)
    var categoryRecipeUiState: CategoryRecipesUiState by mutableStateOf(CategoryRecipesUiState.Loading)
    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)

    /**
     * Call getRandomRecipes() on init so we can display status immediately.
     */
    init {
        getRandomRecipes(0) //TODO: Change it later to 20 or so
    }

    private fun getRandomRecipes(number: Int){
        viewModelScope.launch {
            randomRecipesUiState = try {
                RecipesUiState.Success(repository.getRandomRecipes(API_KEY, number))
            }catch (e: IOException){
                RecipesUiState.Error
            }catch (e: HttpException){
                RecipesUiState.Error
            }
        }
    }

    fun getSearchedRecipes(searchTerm: String){
        val query = applySearchQueries(searchTerm)
        viewModelScope.launch {
           searchedRecipesUiState = try {
               RecipesUiState.Success(repository.getSearchedRecipes(query))
           }catch (e: IOException){
               RecipesUiState.Error
           }catch (e: HttpException){
               RecipesUiState.Error
           }
        }
    }

    fun getCategoryRecipe(category: CategoryListItems){
        val query = applyCategoryQueries(category.query)
        viewModelScope.launch {
            categoryRecipeUiState = try {
                CategoryRecipesUiState.Success(repository.getSearchedRecipes(query), category)
            }catch (e: IOException){
                CategoryRecipesUiState.Error
            }catch (e: HttpException){
                CategoryRecipesUiState.Error
            }
        }
    }

    fun getRecipeById(id: Int){
        viewModelScope.launch {
            recipeUiState = try {
                RecipeUiState.Success(repository.getRecipeById(id, API_KEY))
            }catch (e: IOException){
                RecipeUiState.Error
            }catch (e: HttpException){
                RecipeUiState.Error
            }
        }
    }

    private fun applyCategoryQueries(courseType: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "1" //TODO: Change this to 10 or smth, same for the query below
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = courseType
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    private fun applySearchQueries(searchTerm: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY] = searchTerm
        queries[QUERY_NUMBER] = "1"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}