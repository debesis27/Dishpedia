package com.example.dishpedia.ui.theme.screens.homeScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.viewmodel.RecipeUiState

@Composable
fun HomeScreen(recipesUiState: RecipeUiState){
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
//                Carousel(
//                    count = list.size,
//                    contentWidth = maxWidth,
//                    contentHeight = 200.dp,
//                    content = { modifier, index ->
//                        MyComposableContent(
//                            item = list[index],
//                            modifier = modifier
//                        )
//                    }
//                )
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
                when(recipesUiState){
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
                            .padding(start = 4.dp, end = 4.dp)
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

@Preview
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
        Box(
            modifier = modifier
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Item ${index + 1}",
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun LazyVerticalStaggeredGridPreview(){
    val itemsList = (0..20).toList()
    val itemsIndexedList = listOf("A", "B", "C")

    val itemModifier = Modifier
        .border(1.dp, Color.Blue)
        .wrapContentSize()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        items(itemsList) {
            Text("Item is $it", itemModifier.height(80.dp))
        }
        item {
            Text("Single item", itemModifier.height(100.dp))
        }
        itemsIndexed(itemsIndexedList) { index, item ->
            Text("Item at index $index is $item", itemModifier.height(160.dp))
        }
    }
}
