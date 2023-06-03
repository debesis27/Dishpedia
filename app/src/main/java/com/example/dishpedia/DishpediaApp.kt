package com.example.dishpedia

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dishpedia.ui.screens.HomeScreen
import com.example.dishpedia.ui.screens.RecipeInfoScreen
import com.example.dishpedia.ui.screens.SearchScreen
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.MyRecipeListViewModel
import com.example.dishpedia.viewmodel.RecipesViewModel

enum class DishpediaScreen {
    Home,
    MyRecipes,
    Search,
    RecipeInfo
}

@Composable
fun DishpediaApp(
    recipesViewModel: RecipesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    myRecipeListViewModel: MyRecipeListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DishpediaScreen.Home.name,
    ){
        composable(route = DishpediaScreen.Home.name){
            HomeScreen(recipesViewModel, navController)
        }

        composable(route = DishpediaScreen.Search.name){
            SearchScreen(recipesViewModel, navController)
        }

        composable(route = DishpediaScreen.MyRecipes.name){
            //TODO: Add My Recipes Screen
        }

        composable(route = DishpediaScreen.RecipeInfo.name){
            RecipeInfoScreen(recipesViewModel)
        }
    }

}
