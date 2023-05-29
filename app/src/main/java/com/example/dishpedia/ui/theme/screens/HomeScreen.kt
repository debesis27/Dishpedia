package com.example.dishpedia.ui.theme.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel

@Composable
fun HomeScreen(recipesViewModel: RecipesViewModel){
    Scaffold(
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
                when(val recipesUiState = recipesViewModel.randomRecipeUiState){
                    is RecipeUiState.Success -> RecipeStaggeredGrid(recipes = recipesUiState.recipes)
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
fun RecipePhotoCard(recipe: Recipe, modifier: Modifier = Modifier){
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
