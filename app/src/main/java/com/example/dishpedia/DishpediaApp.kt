package com.example.dishpedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dishpedia.ui.screens.HomeScreen
import com.example.dishpedia.ui.screens.SearchScreen
import com.example.dishpedia.viewmodel.RecipesViewModel

enum class DishpediaScreen(){
    Home,
    MyRecipes,
    Account,
    Search
}

@Composable
fun DishpediaApp(
    recipesViewModel: RecipesViewModel,
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    // TODO: add NavHost
    NavHost(
        navController = navController,
        startDestination = DishpediaScreen.Home.name,
    ){
        composable(route = DishpediaScreen.Home.name){
            HomeScreen(recipesViewModel, navController)
        }

        composable(route = DishpediaScreen.Search.name){
            SearchScreen(recipesViewModel)
        }

        composable(route = DishpediaScreen.MyRecipes.name){
            //TODO: Add My Recipes Screen
        }
    }

}
