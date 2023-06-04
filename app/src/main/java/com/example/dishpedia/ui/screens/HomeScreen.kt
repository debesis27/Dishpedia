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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dishpedia.DishpediaScreen
import com.example.dishpedia.R
import com.example.dishpedia.models.CategoryListItemsProvider
import com.example.dishpedia.models.NavigationDrawerItems
import com.example.dishpedia.models.NavigationDrawerItemsProvider
import com.example.dishpedia.ui.theme.Purple500
import com.example.dishpedia.utils.NavigationDrawer
import com.example.dishpedia.utils.RecipeList
import com.example.dishpedia.viewmodel.CategoryRecipesUiState
import com.example.dishpedia.viewmodel.RecipesUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navItems = NavigationDrawerItemsProvider.navItems

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
                    .padding(top = 14.dp, start = 3.dp, end = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = stringResource(id = R.string.categories),
                    color = Color.DarkGray
                )
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(start = 4.dp)
                        .clip(CircleShape)
                )
            }
            //TODO: Make list and stuffs, then check whether carousel works
            Carousel(
                count = 10,
                parentModifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
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
                        .background(Color.White)
                        .clickable {
                            recipesViewModel.getCategoryRecipe(category)
//                                navController.navigate(DishpediaScreen.RecipeInfo.name)
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
                            modifier = Modifier.width(150.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 14.dp, start = 3.dp, end = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = when (recipesViewModel.categoryRecipeUiState) {
                        is CategoryRecipesUiState.Loading -> stringResource(id = R.string.featured)
                        is CategoryRecipesUiState.Success -> (recipesViewModel.categoryRecipeUiState as CategoryRecipesUiState.Success).category.text
                        is CategoryRecipesUiState.Error -> stringResource(id = R.string.error)
                    },
                    color = Color.DarkGray
                )
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .padding(start = 4.dp)
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
                }

                is CategoryRecipesUiState.Success -> RecipeList(
                    categoryUiState.recipes,
                    recipesViewModel,
                    navController
                )
            }

        }
    }
}

@Composable
fun Carousel(
    count: Int,
    parentModifier: Modifier = Modifier
        .fillMaxWidth()
        .height(540.dp),
    contentWidth: Dp,
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {

    BoxWithConstraints(modifier = parentModifier) {
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
fun HomeScreenAppBar(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
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
        },
        actions = {
            IconButton(onClick = { navController.navigate(DishpediaScreen.Search.name) }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(id = R.string.search_button),
                    tint = Color.White
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CarouselPreview(){
    Carousel(
        count = 10,
        parentModifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentWidth = 250.dp,
        contentHeight = 200.dp
    ) { modifier, index ->
        val image = when(index){
            0 -> R.drawable.maincourse
            1 -> R.drawable.bread
            2 -> R.drawable.appetizer
            3 -> R.drawable.beverage
            4 -> R.drawable.breakfast
            5 -> R.drawable.desserts
            6 -> R.drawable.salad
            7 -> R.drawable.sidedish
            8 -> R.drawable.snacks
            else -> R.drawable.soup
        }
        val text = when(index){
            0 -> R.string.maincourse
            1 -> R.string.bread
            2 -> R.string.appetizer
            3 -> R.string.beverage
            4 -> R.string.breakfast
            5 -> R.string.desserts
            6 -> R.string.salad
            7 -> R.string.sidedish
            8 -> R.string.snacks
            else -> R.string.soup
        }
        Box(
            modifier = modifier
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                )
                Text(
                    text = stringResource(id = text),
                    modifier = Modifier.width(150.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
