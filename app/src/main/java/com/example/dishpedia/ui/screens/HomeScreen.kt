package com.example.dishpedia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dishpedia.R
import com.example.dishpedia.models.CategoryListItemsProvider
import com.example.dishpedia.models.NavigationDrawerItemsProvider
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.utils.NavigationDrawer
import com.example.dishpedia.utils.RecipeList
import com.example.dishpedia.viewmodel.CategoryRecipesUiState
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.google.accompanist.placeholder.placeholder

@Composable
fun HomeScreen(
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = NavigationDrawerItemsProvider.navItems

    recipesViewModel.recipeUiState = RecipeUiState.Loading

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeScreenAppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                navController = navController
            )
        },
        drawerContent = { NavigationDrawer(navItems, navController, coroutineScope, scaffoldState) },
        drawerGesturesEnabled = true
    ) {
        Column(modifier = Modifier.padding(it)) {
            Row(
                modifier = Modifier
                    .padding(top = 14.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(end = 10.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = stringResource(id = R.string.categories),
                    style = MaterialTheme.typography.h3,
                    color = Color.DarkGray
                )
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(start = 10.dp)
                        .clip(CircleShape)
                )
            }
            Carousel(
                count = 10,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colors.background),
                contentWidth = 250.dp,
                contentHeight = 200.dp
            ) { modifier, index ->
                val category = when (index) {
                    0 -> CategoryListItemsProvider.mainCourse
                    1 -> CategoryListItemsProvider.bread
                    2 -> CategoryListItemsProvider.soup
                    3 -> CategoryListItemsProvider.dessert
                    4 -> CategoryListItemsProvider.beverage
                    5 -> CategoryListItemsProvider.appetizer
                    6 -> CategoryListItemsProvider.breakfast
                    7 -> CategoryListItemsProvider.salad
                    8 -> CategoryListItemsProvider.sideDish
                    else -> CategoryListItemsProvider.snacks
                }

                Box(
                    modifier = modifier
                        .clickable {
                            recipesViewModel.getCategoryRecipe(category)
                        },
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = category.image),
                            contentDescription = null,
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                        )
                        Text(
                            text = category.text,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.width(150.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 14.dp, start = 10.dp, end = 10.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(end = 10.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = when (recipesViewModel.categoryRecipeUiState) {
                        is CategoryRecipesUiState.Loading -> stringResource(id = R.string.featured)
                        is CategoryRecipesUiState.Success -> (recipesViewModel.categoryRecipeUiState as CategoryRecipesUiState.Success).category.text
                        is CategoryRecipesUiState.Error -> stringResource(id = R.string.error)
                    },
                    style = MaterialTheme.typography.h3,
                    color = Color.DarkGray
                )
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(start = 10.dp)
                        .clip(CircleShape)
                )
            }

            when (val categoryUiState = recipesViewModel.categoryRecipeUiState) {
                is CategoryRecipesUiState.Loading -> when (val recipesUiState =
                    recipesViewModel.randomRecipesUiState) {
                    is RecipesUiState.Success -> RecipeList(
                        recipesUiState.recipes,
                        recipesViewModel,
                        navController
                    )
                    is RecipesUiState.Loading -> LoadingScreen()
                    is RecipesUiState.Error -> ErrorScreen()
                }

                is CategoryRecipesUiState.Success -> RecipeList(
                    categoryUiState.recipes,
                    recipesViewModel,
                    navController
                )
                is CategoryRecipesUiState.Error -> ErrorScreen()
            }

        }
    }
}

@Composable
fun HomeScreenAppBar(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
){
    TopAppBar(
        title = { Text(
            text = stringResource(id = R.string.app_name),
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
        },
        actions = {
            IconButton(onClick = { navController.navigate(NavigationItemsProvider.Search.route) }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(id = R.string.search_button),
                    tint = Color.Black
                )
            }
        }
    )
}

@Composable
fun Carousel(
    count: Int,
    modifier: Modifier = Modifier,
    contentWidth: Dp,
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {

    BoxWithConstraints(modifier = modifier) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                count = count
            ) { globalIndex ->
                content(
                    index = globalIndex % count,
                    modifier = Modifier
                        .width(contentWidth)
                        .height(contentHeight)
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(
            text = "Sorry, Please try again later",
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
private fun LoadingScreen(){
    LazyColumn(
        modifier = Modifier.padding(top = 10.dp, start = 14.dp, end = 14.dp, bottom = 10.dp)
    ){
        items(10){
            Card(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 40.dp)
                    .height(300.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = true,
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp),
                        highlight = PlaceholderHighlight.fade()
                    ),
                elevation = 4.dp
            ) {

            }
        }
    }
}
