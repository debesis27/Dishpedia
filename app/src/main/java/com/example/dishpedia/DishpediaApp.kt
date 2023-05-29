package com.example.dishpedia

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dishpedia.ui.theme.screens.HomeScreen
import com.example.dishpedia.ui.theme.screens.SearchScreen
import com.example.dishpedia.viewmodel.RecipesViewModel

enum class DishpediaScreen(){
    Dishpedia,                      //HomePage
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = DishpediaScreen.valueOf(backStackEntry?.destination?.route ?: DishpediaScreen.Dishpedia.name)

    Scaffold(
        topBar = {
            DishpediaAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        // TODO: add NavHost
        SearchScreen(recipesViewModel)
    }
}

@Composable
fun DishpediaAppBar(
    currentScreen: DishpediaScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(currentScreen.name) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen.name == "Dishpedia"){
                IconButton(onClick = { /*TODO: Add SearchPage*/ }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_button)
                    )
                }
            }
        }
    )
}
