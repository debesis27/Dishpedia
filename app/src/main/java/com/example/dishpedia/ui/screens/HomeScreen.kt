package com.example.dishpedia.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.material.icons.rounded.Home
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.DishpediaScreen
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationDrawerItems
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.ui.theme.Purple500
import com.example.dishpedia.viewmodel.RecipesUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navItems = listOf(
        NavigationDrawerItems(
            name = "Home",
            route = "Home",
        ),
        NavigationDrawerItems(
            name = "My Recipes",
            route = "MyRecipes",
        ),
        NavigationDrawerItems(
            name = "Account",
            route = "Account",
        )
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { HomeScreenAppBar(coroutineScope = coroutineScope, scaffoldState = scaffoldState, navController = navController) },
        drawerContent = { HomeScreenDrawer(navItems, navController, coroutineScope, scaffoldState) },
        drawerGesturesEnabled = true,
        content = {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .padding(top = 14.dp,start = 3.dp, end = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
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
                        modifier = Modifier
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
                Row(
                    modifier = Modifier
                        .padding(top = 14.dp,start = 3.dp, end = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .padding(end = 4.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = stringResource(id = R.string.featured),
                        modifier = Modifier
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

                //TODO: Add the staggered list here
                when(val recipesUiState = recipesViewModel.randomRecipesUiState){
                    is RecipesUiState.Success -> RecipeStaggeredGrid(recipes = recipesUiState.recipes)
                }
            }
        }
    )
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
                count = count,
                itemContent = { globalIndex ->
                    content(
                        index = globalIndex % count,
                        modifier = Modifier
                            .width(contentWidth)
                            .height(contentHeight)
                    )
                }
            )
        }
    }
}

@Composable
fun RecipePhotoCard(
    recipe: Recipe,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = 8.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(recipe.image)
                .crossfade(true)
                .build(),
            contentDescription = recipe.title,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentScale = ContentScale.FillBounds
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeStaggeredGrid(
    recipes: Recipes,
    modifier: Modifier = Modifier
){
    val items = recipes.recipes

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(items){
            recipe -> RecipePhotoCard(recipe = recipe)
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

@Composable
fun HomeScreenDrawer(
    navItems: List<NavigationDrawerItems>,
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(126.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.guest_icon),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        navItems.forEach{ item ->
            val selected = item.route == backStackEntry?.destination?.route

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate(item.route)
                    },
                backgroundColor = if (selected) Purple500 else Color.White,
                elevation = 0.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                }
            }
        }
    }
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
