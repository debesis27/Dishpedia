package com.example.dishpedia

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.ui.screens.HomeScreen
import com.example.dishpedia.ui.screens.MyRecipeEntryScreen
import com.example.dishpedia.ui.screens.MyRecipeScreen
import com.example.dishpedia.ui.screens.RecipeInfoScreen
import com.example.dishpedia.ui.screens.SearchScreen
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.RecipesViewModel

@Composable
fun DishpediaApp(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()
    val recipesViewModel: RecipesViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = NavigationItemsProvider.Home.route,
    ){
        composable(route = NavigationItemsProvider.Home.route){
            HomeScreen(recipesViewModel, navController)
        }

        composable(route = NavigationItemsProvider.Search.route){
            SearchScreen(recipesViewModel, navController)
        }

        composable(route = NavigationItemsProvider.MyRecipes.route){
            MyRecipeScreen(navController)
        }

        composable(route = NavigationItemsProvider.recipeInfo.route){
            RecipeInfoScreen(recipesViewModel)
        }

        composable(route = NavigationItemsProvider.MyRecipeEntry.route){
            MyRecipeEntryScreen(navController)
        }
    }
}
