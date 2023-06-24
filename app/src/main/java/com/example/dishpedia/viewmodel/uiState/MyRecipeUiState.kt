package com.example.dishpedia.viewmodel.uiState

import android.net.Uri
import androidx.core.net.toUri
import com.example.dishpedia.data.MyRecipe

/**
 * Represents Ui State for a Recipe
 */
data class MyRecipeUiState(
    val id: Int = 0,
    val image: Uri = "".toUri(),
    val title: String = "",
    val summary: String = "",
    val readyInMinutes: String = "",
    val category: String = "",
    val vegetarian: Boolean = false,
    val servings: String = "",
    val ingredient: String = "",
    val instructions: String = "",
    val calorieAmount: String = "",
    val fatAmount: String = "",
    val carbohydrateAmount: String = "",
    val proteinAmount: String = "",
    val actionEnabled: Boolean = false
)

/**
 * Extension function to convert [MyRecipeUiState] to [MyRecipe]
 */
fun MyRecipeUiState.toMyRecipe(): MyRecipe = MyRecipe(
    id = id,
    image = image.toString(),
    title = title,
    summary = summary,
    readyInMinutes = readyInMinutes.toDoubleOrNull() ?: 0.0,
    category= category,
    vegetarian = vegetarian,
    servings = servings.toDoubleOrNull() ?: 0.0,
    ingredient = ingredient,
    instructions = instructions,
    calorieAmount = calorieAmount,
    fatAmount = fatAmount,
    carbohydrateAmount = carbohydrateAmount,
    proteinAmount = proteinAmount
)

/**
 * Extension function to convert [MyRecipe] to [MyRecipeUiState]
 */
fun MyRecipe.toMyRecipeUiState(actionEnabled: Boolean = false): MyRecipeUiState = MyRecipeUiState(
    id = id,
    image = image.toUri(),
    title = title,
    summary = summary,
    readyInMinutes = readyInMinutes.toString(),
    category= category,
    vegetarian = vegetarian,
    servings = servings.toString(),
    ingredient = ingredient,
    instructions = instructions,
    calorieAmount = calorieAmount,
    fatAmount = fatAmount,
    carbohydrateAmount = carbohydrateAmount,
    proteinAmount = proteinAmount,
    actionEnabled = actionEnabled
)

/**
 * Extension function to check whether title, ingredients and instructions are empty
 */
fun MyRecipeUiState.isValid(): Boolean {
    return title.isNotBlank() && readyInMinutes.isNotBlank() && category.isNotBlank() && ingredient.isNotBlank() && instructions.isNotBlank()
}
