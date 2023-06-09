package com.example.dishpedia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.data.MyRecipe
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
    val myRecipeUiState by myRecipeListViewModel.myRecipeListUiState.collectAsState()
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
                    tint = MaterialTheme.colors.surface
                )
            }
        },
        drawerContent = { NavigationDrawer(navItems, navController, coroutineScope, scaffoldState) },
        drawerGesturesEnabled = true,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
        ) {
            items(myRecipeUiState.myRecipeList){myRecipe ->
                MyRecipeCard(
                    myRecipe = myRecipe,
                    myRecipeListViewModel = myRecipeListViewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MyRecipeCard(
    myRecipe: MyRecipe,
    myRecipeListViewModel: MyRecipeListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp, start = 16.dp, end = 18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate("${NavigationItemsProvider.MyRecipeDetails.route}/${myRecipe.id}")
            },
        elevation = 2.dp
    ) {
        Row{
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(myRecipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = myRecipe.title,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(id = R.drawable.image_placeholder),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = myRecipe.title,
                    modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = myRecipe.summary,
                    style = MaterialTheme.typography.h3,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
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
        title = { Text(
            text = stringResource(id = R.string.my_recipes),
            style = MaterialTheme.typography.h1
        ) },
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
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
