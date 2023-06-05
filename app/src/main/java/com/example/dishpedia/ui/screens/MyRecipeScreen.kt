package com.example.dishpedia.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationDrawerItemsProvider
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.utils.NavigationDrawer
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.MyRecipeListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyRecipeScreen(
    navController: NavController,
    myRecipeListViewModel: MyRecipeListViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = NavigationDrawerItemsProvider.navItems

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MyRecipeScreenAppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(NavigationItemsProvider.MyRecipeEntry.route) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        drawerContent = { NavigationDrawer(navItems, navController, coroutineScope, scaffoldState) },
        drawerGesturesEnabled = true,
    ) {
        Column(modifier = Modifier.padding(it)) {
            //TODO: Add LazyColumn instead of Column and display all MyRecipes
        }
    }
}

@Composable
fun MyRecipeScreenAppBar(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
){
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.menu_button)
                )
            }
        }
    )
}
