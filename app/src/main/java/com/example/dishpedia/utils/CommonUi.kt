package com.example.dishpedia.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationItem
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.models.Nutrients
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.example.dishpedia.viewmodel.TabsViewModel
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import de.charlex.compose.HtmlText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

/**
 * Composable that shows a list of recipes using LazyColumn
 */
@Composable
fun RecipeList(
    recipes: Recipes,
    recipesViewModel: RecipesViewModel,
    navController: NavController
){
    LazyColumn(
        modifier = Modifier.padding(top = 10.dp, start = 14.dp, end = 14.dp, bottom = 10.dp)
    ){
        items(recipes.recipes){recipe ->
            RecipeCard(recipe, recipesViewModel, navController)
        }
    }
}

/**
 * Composable that gives a short info of the recipe. It is used by [RecipeList]
 */
@Composable
private fun RecipeCard(
    recipe: Recipe,
    recipesViewModel: RecipesViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val diet = when(recipe.vegetarian){
        true -> "veg"
        false -> "non-veg"
    }

    Card(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 40.dp)
            .height(300.dp)
            .fillMaxWidth()
            .clickable {
                recipesViewModel.getRecipeById(recipe.id)
                navController.navigate(NavigationItemsProvider.recipeInfo.route)
            }
        ,
        elevation = 4.dp
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                error = painterResource(id = R.drawable.ic_connection_error),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = recipe.title,
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 3.dp),
                style = MaterialTheme.typography.h2
            )
            Text(
                text = "${recipe.readyInMinutes} min · $diet",
                modifier = Modifier.padding(horizontal = 14.dp),
                style = MaterialTheme.typography.h3
            )
        }
    }
}

/**
 * Composable that shows the Navigation drawer
 */
@Composable
fun NavigationDrawer(
    navItems: List<NavigationItem>,
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
                backgroundColor = if (selected) MaterialTheme.colors.background else MaterialTheme.colors.surface,
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
                        modifier = Modifier.padding(start = 24.dp),
                        style = MaterialTheme.typography.h2
                    )
                }
            }
        }
    }
}

/**
 * Composable that shows the complete information of a recipe using [RecipeInfoCard], [SummaryScreen], [IngredientsScreen] and [InstructionsScreen]
 */
@Composable
fun RecipeInfo(
    image: String?,
    title: String,
    category: String,
    cookTime: String,
    vegetarian: Boolean,
    summary: String,
    servings: String,
    ingredients: List<String>,
    instructions: List<String>,
    nutrients: List<Nutrients>,
    tabsViewModel: TabsViewModel,
    modifier: Modifier = Modifier,
){
    val tabIndex = tabsViewModel.tabIndex.observeAsState()
    var sizeOfRecipeInfoCard by remember{ mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val nutrientsList: ArrayList<Nutrients> = ArrayList()
    nutrients.forEach{ nutrient ->
        if(nutrient.name == "Calories" || nutrient.name == "Fat" || nutrient.name == "Carbohydrates" || nutrient.name == "Protein"){
            nutrientsList.add(nutrient)
        }
    }
    val temp = nutrientsList[1]
    nutrientsList[1] = nutrientsList[2]
    nutrientsList[2] = temp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.background)
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.large,
            elevation = 0.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                error = painterResource(id = R.drawable.image_placeholder),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop
            )
        }

        RecipeInfoCard(
            title = title,
            category = category,
            cookTime = cookTime,
            diet = if(vegetarian){
                "veg"
            } else if(!vegetarian){
                "non-veg"
            } else {
                ""
            },
            spacerHeight = 250.dp,
            modifier = Modifier.onGloballyPositioned {
                sizeOfRecipeInfoCard = with(density){
                    it.size.height.toDp()
                }
            }
        )

        Column {
            Spacer(modifier = Modifier.height(250.dp + sizeOfRecipeInfoCard))
            TabRow(
                selectedTabIndex = tabIndex.value!!,
                backgroundColor = Color.Transparent,
                indicator = @Composable {
                    TabRowDefaults.Indicator(
                        height = 0.dp,
                        color = Color.Transparent
                    )
                },
                divider = @Composable {
                    TabRowDefaults.Divider(
                        thickness = 0.dp,
                        color = Color.Transparent
                    )
                }
            ) {
                tabsViewModel.tabs.forEachIndexed{ index, title ->
                    Tab(
                        selected = tabIndex.value!! == index,
                        modifier = Modifier
                            .padding(top = 23.dp, bottom = 12.dp),
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.secondary,
                        onClick = { tabsViewModel.updateTabIndex(index) }
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h2
                        )
                    }
                }
            }

            when(tabIndex.value){
                0 -> SummaryScreen(
                    summary = summary,
                    nutrients = nutrientsList,
                    tabsViewModel = tabsViewModel
                )
                1 -> IngredientsScreen(
                    servings = servings,
                    ingredients = ingredients,
                    tabsViewModel = tabsViewModel
                )
                2 -> InstructionsScreen(
                    instructions = instructions,
                    tabsViewModel =  tabsViewModel
                )
            }
        }
    }
}

/**
 * Composable that shows the name, category, cook-time and diet.
 */
@Composable
fun RecipeInfoCard(
    title: String,
    category: String = "",
    cookTime: String = "",
    diet: String = "",
    spacerHeight: Dp = 0.dp,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spacerHeight))

        Card(
            modifier = modifier
                .width(350.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(1.dp)
                        .clip(CircleShape)
                )

                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tags_24),
                        contentDescription = null
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.time_forward_24),
                        contentDescription = null
                    )
                    Text(
                        text = "$cookTime min",
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.broccoli_24),
                        contentDescription = null
                    )
                    Text(
                        text = diet,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Composable to show the nutrition of a recipe on a Card
 */
@Composable
fun NutritionCard(
    modifier: Modifier = Modifier,
    nutrients: List<Nutrients>
){
   Card(
       modifier = modifier
           .padding(horizontal = 0.dp, vertical = 32.dp),
       backgroundColor = MaterialTheme.colors.surface,
       elevation = 4.dp
   ) {
       Column(
           modifier = Modifier
               .padding(horizontal = 16.dp, vertical = 14.dp),
           verticalArrangement = Arrangement.Center
       ) {
           Text(
               text = stringResource(id = R.string.nutrition),
               style = MaterialTheme.typography.h2,
               modifier = Modifier.padding(start = 1.dp, bottom = 4.dp)
           )
           Divider(
               color = Color.Black,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 1.dp)
                   .height(1.dp)
                   .clip(CircleShape)
           )
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(start = 1.dp, end = 1.dp, top = 14.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               nutrients.forEach { nutrient ->
                   Column(
                       modifier = Modifier.padding(end = 4.dp),
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Text(
                           text = nutrient.name,
                           style = MaterialTheme.typography.h3
                       )
                       Text(
                           text = "${nutrient.amount} ${nutrient.unit}",
                           style = MaterialTheme.typography.h3,
                           color = MaterialTheme.colors.onSecondary,
                           modifier = Modifier.padding(vertical = 14.dp)
                       )
                   }
               }
           }
       }
   }
}

/**
 * Composable to show the summary and nutrition of a recipe using [ExpandableText] and [NutritionCard]
 */
@Composable
fun SummaryScreen(
    summary: String,
    nutrients: List<Nutrients>,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp, end = 23.dp, bottom = 20.dp)
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ExpandableText(
            text = summary,
            minimizedMaxLines = 5
        )
        if (showNutritionCard(nutrients)) NutritionCard(nutrients = nutrients)
    }
}

/**
 * Composable to show the servings and ingredients of a recipe
 */
@Composable
fun IngredientsScreen(
    servings: String,
    ingredients: List<String>,
    tabsViewModel: TabsViewModel
){
    var i = 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp, end = 23.dp, bottom = 20.dp)
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(
                text = "Serves: ",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = servings,
                style = MaterialTheme.typography.h2
            )
        }

        ingredients.forEach{ item ->
            Text(
                text = "$i) $item",
                fontSize = 18.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
            i += 1
        }
    }
}

/**
 * Composable to show the procedure of a recipe
 */
@Composable
fun InstructionsScreen(
    instructions: List<String>,
    tabsViewModel: TabsViewModel
){
    var i = 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp, end = 23.dp, bottom = 20.dp)
            .draggable(
                state = tabsViewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    tabsViewModel.updateTabIndexBasedOnSwipe()
                }
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ){
        instructions.forEach{ item ->
            Text(
                text = "$i) $item",
                fontSize = 18.sp,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            i += 1
        }
    }
}

/**
 * Composable for creating or updating a recipe in the database
 */
@Composable
fun MyRecipeEditBody(
    context: Context,
    myRecipeUiState: MyRecipeUiState,
    onRecipeValueChange: (MyRecipeUiState) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        MyRecipeInputForm(
            context = context,
            myRecipeUiState = myRecipeUiState,
            onRecipeValueChange = onRecipeValueChange
        )
        Button(
            onClick = onSaveClick,
            enabled = myRecipeUiState.actionEnabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        ) {
            Text(text = stringResource(id = R.string.save_button))
        }
    }
}

/**
 * Composable consisting of all text fields in [MyRecipeEditBody]
 */
@Composable
fun MyRecipeInputForm(
    context: Context,
    myRecipeUiState: MyRecipeUiState,
    onRecipeValueChange: (MyRecipeUiState) -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            ImagePicker(
                context = context,
                myRecipeUiState = myRecipeUiState,
                onRecipeValueChange = onRecipeValueChange
            )
        }

        OutlinedTextField(
            value = myRecipeUiState.title,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(title = it)) },
            label = { Text(stringResource(id = R.string.recipe_title_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.summary,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(summary = it)) },
            label = { Text(stringResource(id = R.string.recipe_summary_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        )

        OutlinedTextField(
            value = myRecipeUiState.readyInMinutes,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(readyInMinutes = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_ready_in_minutes_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.category,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(category = it)) },
            label = { Text(stringResource(id = R.string.recipe_cuisines_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.calorieAmount,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(calorieAmount = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_calories_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.fatAmount,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(fatAmount = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_fat_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.carbohydrateAmount,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(carbohydrateAmount = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_carbohydrates_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = myRecipeUiState.proteinAmount,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(proteinAmount = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_protein_label))},
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.recipe_vegetarian_label))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = myRecipeUiState.vegetarian,
                        onClick = { onRecipeValueChange(myRecipeUiState.copy(vegetarian = true)) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = myRecipeUiState.vegetarian,
                    onClick = { onRecipeValueChange(myRecipeUiState.copy(vegetarian = true)) }
                )
                Text(text = "vegetarian")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = !myRecipeUiState.vegetarian,
                        onClick = { onRecipeValueChange(myRecipeUiState.copy(vegetarian = false)) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = !myRecipeUiState.vegetarian,
                    onClick = { onRecipeValueChange(myRecipeUiState.copy(vegetarian = false)) }
                )
                Text(text = "non-vegetarian")
            }
        }

        OutlinedTextField(
            value = myRecipeUiState.servings,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(servings = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(id = R.string.recipe_servings_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        //TODO: Add an OutlinedTextField with trailing icon of add, then add more OutlinedTextField depending on it
        //Tried doing it, but didn't work out since OutlinedTextField only allows primitive data types (its a known bug apparently) (see AddTextField function)
        OutlinedTextField(
            value = myRecipeUiState.ingredient,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(ingredient = it)) },
            label = { Text(stringResource(id = R.string.recipe_ingredients_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            maxLines = 30
        )

        OutlinedTextField(
            value = myRecipeUiState.instructions,
            onValueChange = { onRecipeValueChange(myRecipeUiState.copy(instructions = it)) },
            label = { Text(stringResource(id = R.string.recipe_instructions_label)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            maxLines = 30
        )
    }
}

/**
 * Composable to pick image from the gallery
 */
@Composable
fun ImagePicker(
    context: Context,
    myRecipeUiState: MyRecipeUiState,
    onRecipeValueChange: (MyRecipeUiState) -> Unit
){
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val copiedImagePath = copyImageToInternalStorage(context, uri ?: "".toUri())
            if (copiedImagePath != null) {
                onRecipeValueChange(myRecipeUiState.copy(image = copiedImagePath.toUri()))
            }
        }
    )

    AsyncImage(
        model = myRecipeUiState.image,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Inside,
        placeholder = painterResource(id = R.drawable.image_placeholder)
    )

    Button(colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        onClick = {
        photoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }) {
        Text(text = "Pick photo")
    }
}

fun copyImageToInternalStorage(
    context: Context,
    imageUri: Uri
): String? {
    // Generate a unique file name
    val fileName = "image_${System.currentTimeMillis()}"

    // Get the file extension from the URI
    val fileExtension = getFileExtension(context.contentResolver, imageUri)

    // Create a destination file in the internal storage
    val internalStorageDir = context.filesDir
    val destinationFile = File(internalStorageDir, "$fileName.$fileExtension")

    // Open input stream from the image URI
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null
    try {
        inputStream = contentResolver.openInputStream(imageUri)

        // Decode the input stream into a Bitmap
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

        // Create a file output stream
        val fileOutputStream = FileOutputStream(destinationFile)

        // Determine the image format based on file extension
        val compressFormat = when (fileExtension.lowercase(Locale.ROOT)) {
            "jpg", "jpeg" -> Bitmap.CompressFormat.JPEG
            "png" -> Bitmap.CompressFormat.PNG
            else -> Bitmap.CompressFormat.JPEG // Default to JPEG format
        }

        // Compress the bitmap to the file output stream with desired format and quality
        bitmap.compress(compressFormat, 100, fileOutputStream)

        // Close the file output stream
        fileOutputStream.close()

        // Return the absolute path of the saved image file
        return destinationFile.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        // Close the input stream
        inputStream?.close()
    }

    return null
}

// Helper function to get file extension from the URI
private fun getFileExtension(contentResolver: ContentResolver, uri: Uri): String {
    val mimeType = contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        ?: MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        ?: ""
}

/**
 * Composable to show collapsable text
 */
@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    minimizedMaxLines: Int,
    style: TextStyle = MaterialTheme.typography.body1
) {
    var expanded by remember { mutableStateOf(false) }
    var hasVisualOverflow by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        HtmlText(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            onTextLayout = { if(it.hasVisualOverflow) hasVisualOverflow = true },
            style = style,
            fontSize = 17.sp
        )
        if (hasVisualOverflow && !expanded) {
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(
                    modifier = Modifier
                        .width(48.dp)
                        .height(18.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colors.background)
                            )
                        )
                )
                Text(
                    text = "Show More",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .padding(start = 4.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { expanded = !expanded }
                        )
                )
            }
        }else if (hasVisualOverflow && expanded){
            Row(
                modifier = Modifier.align(Alignment.BottomEnd),
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(
                    modifier = Modifier
                        .width(48.dp)
                        .height(20.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colors.background)
                            )
                        )
                )
                Text(
                    text = "Show Less",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .padding(start = 4.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { expanded = !expanded }
                        )
                )
            }
        }
    }
}

//@Composable
//fun AddTextField(
//    myRecipeUiState: MyRecipeUiState,
//    onRecipeValueChange: (MyRecipeUiState) -> Unit,
//){
//    val ingredients = remember{ ArrayList<String>() }
//    val textFields = remember{ mutableStateOf(1) }
//    ingredients.add("")
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ){
//            for (i in 0 until textFields.value){
//                OutlinedTextField(
//                    value = ingredients[i],
//                    onValueChange = {
//                        ingredients[i] = it
//                        onRecipeValueChange(myRecipeUiState.copy(ingredient = ingredients))
//                    },
//                    label = { Text(text = "Ingredient: ${i+1}") },
//                    modifier = Modifier.fillMaxWidth(),
//                    trailingIcon = {
//                        if(i == textFields.value-1){
//                            Icon(
//                                imageVector = Icons.Default.Add,
//                                contentDescription = null,
//                                modifier = Modifier.clickable {
////                                    ingredients.add("")
//                                    textFields.value = textFields.value + 1
//                                }
//                            )
//                        }
//                    }
//                )
//        }
//    }
//}
