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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationItem
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.models.Recipe
import com.example.dishpedia.models.Recipes
import com.example.dishpedia.ui.theme.Purple500
import com.example.dishpedia.viewmodel.RecipeUiState
import com.example.dishpedia.viewmodel.RecipesViewModel
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
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
            .padding(16.dp),
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
        //TODO: Add a method to save the obtained image
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
            enabled = true,
            singleLine = true
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
        } //TODO: Add placeholder uri here as well
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

fun copyImageToInternalStorage(context: Context, imageUri: Uri): String? {
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

//TODO: Make a better Error Screen
@Composable
fun ErrorScreen(){
    Box(modifier = Modifier.fillMaxWidth()){
        Text(text = "ERROR")
    }
}
