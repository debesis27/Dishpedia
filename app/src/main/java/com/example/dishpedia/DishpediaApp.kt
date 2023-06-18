package com.example.dishpedia

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.ui.screens.HomeScreen
import com.example.dishpedia.ui.screens.MyRecipeDetailsScreen
import com.example.dishpedia.ui.screens.MyRecipeEditScreen
import com.example.dishpedia.ui.screens.MyRecipeEntryScreen
import com.example.dishpedia.ui.screens.MyRecipeScreen
import com.example.dishpedia.ui.screens.RecipeInfoScreen
import com.example.dishpedia.ui.screens.SearchScreen
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.RecipesViewModel

@Composable
fun DishpediaApp(
    context: Context,
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

        composable(route = NavigationItemsProvider.recipeInfo.route){
            RecipeInfoScreen(
                recipeViewModel = recipesViewModel,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable(route = NavigationItemsProvider.MyRecipes.route){
            MyRecipeScreen(
                navController = navController
            )
        }

        composable(route = NavigationItemsProvider.MyRecipeEntry.route){
            MyRecipeEntryScreen(
                context = context,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(
            route = NavigationItemsProvider.MyRecipeEdit.routeWithArgs,
            arguments = listOf(navArgument(NavigationItemsProvider.MyRecipeEdit.recipeIdArg){
                type = NavType.IntType
            })
        ){
            MyRecipeEditScreen(
                context = context,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = NavigationItemsProvider.MyRecipeDetails.routeWithArgs,
            arguments = listOf(navArgument(NavigationItemsProvider.MyRecipeDetails.recipeIdArg){
                type = NavType.IntType
            })
        ){
            MyRecipeDetailsScreen(
                navigateToEditMyRecipe = { navController.navigate("${NavigationItemsProvider.MyRecipeEdit.route}/$it") },
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
