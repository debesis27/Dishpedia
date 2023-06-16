package com.example.dishpedia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
import com.example.dishpedia.utils.IngredientsScreen
import com.example.dishpedia.utils.InstructionsScreen
import com.example.dishpedia.utils.RecipeInfoCard
import com.example.dishpedia.utils.SummaryScreen
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.MyRecipeDetailViewModel
import com.example.dishpedia.viewmodel.TabsViewModel
import com.example.dishpedia.viewmodel.uiState.MyRecipeUiState
import kotlinx.coroutines.launch

@Composable
fun MyRecipeDetailsScreen(
    navigateToEditMyRecipe: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    myRecipeDetailViewModel: MyRecipeDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    tabsViewModel: TabsViewModel = viewModel()
){
    val tabIndex = tabsViewModel.tabIndex.observeAsState()
    val myRecipeUiState by myRecipeDetailViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmation by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = MaterialTheme.shapes.large,
            elevation = 0.dp
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(myRecipeUiState.image)
                    .crossfade(true)
                    .build(),
                contentDescription = myRecipeUiState.title,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.Crop
            )
        }

        RecipeInfoCard(
            title = myRecipeUiState.title,
            category = myRecipeUiState.category,
            cookTime = myRecipeUiState.readyInMinutes,
            diet = if(myRecipeUiState.vegetarian){
                "veg"
            } else if(myRecipeUiState.vegetarian.not()){
                "non-veg"
            } else {
                ""
            },
            spacerHeight = 250.dp
        )

        Column{
            Spacer(modifier = Modifier.height(400.dp))
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
                            .padding(bottom = 12.dp),
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
                    summary = myRecipeUiState.summary,
                    tabsViewModel = tabsViewModel
                )
                1 -> IngredientsScreen(
                    servings = myRecipeUiState.servings,
                    ingredients = myRecipeUiState.ingredient.lines(),
                    tabsViewModel = tabsViewModel
                )
                2 -> InstructionsScreen(
                    instructions = myRecipeUiState.instructions.lines(),
                    tabsViewModel =  tabsViewModel
                )
            }
        }

        MyRecipeDetailsScreenAppBar(
            myRecipeUiState = myRecipeUiState,
            navigateUp = onNavigateUp,
            navigateToEditMyRecipe = navigateToEditMyRecipe,
            onDelete = {
                deleteConfirmation = true
            }
        )

        if(deleteConfirmation){
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmation = false
                    coroutineScope.launch {
                        myRecipeDetailViewModel.deleteMyRecipe()
                        onNavigateUp()
                    }
                },
                onDeleteCancel = { deleteConfirmation = false }
            )
        }
    }
}

@Composable
fun MyRecipeDetailsScreenAppBar(
    myRecipeUiState: MyRecipeUiState,
    navigateUp: () -> Unit,
    navigateToEditMyRecipe: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(text = "") },
        modifier = modifier,
        backgroundColor = Color.Transparent,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { navigateToEditMyRecipe(myRecipeUiState.id) }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Recipe",
                    tint = Color.White
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_button),
                    tint = Color.White
                )
            }
        },
        elevation = 0.dp
    )
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
