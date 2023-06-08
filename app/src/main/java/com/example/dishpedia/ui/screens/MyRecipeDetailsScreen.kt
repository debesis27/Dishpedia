package com.example.dishpedia.ui.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dishpedia.R
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

    Scaffold(
        topBar = {
            MyRecipeDetailsScreenAppBar(
                myRecipeUiState = myRecipeUiState,
                navigateUp = onNavigateUp,
                navigateToEditMyRecipe = navigateToEditMyRecipe,
                onDelete = {
                    deleteConfirmation = true
                }
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex.value!!) {
                tabsViewModel.tabs.forEachIndexed{ index, title ->
                    Tab(
                        selected = tabIndex.value!! == index,
                        onClick = { tabsViewModel.updateTabIndex(index) }
                    ) {
                        Text(text = title)
                    }
                }
            }

            when(tabIndex.value){
                0 -> SummaryScreen(myRecipeUiState, tabsViewModel)
                1 -> IngredientsScreen(myRecipeUiState, tabsViewModel)
                2 -> InstructionsScreen(myRecipeUiState, tabsViewModel)
            }
            if(deleteConfirmation){
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmation = false
                        coroutineScope.launch {
                            myRecipeDetailViewModel.deleteMyRecipe()
                            onNavigateUp()
                        }
                    },
                    onDeleteCancel = { deleteConfirmation = false })
            }
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
        title = { Text(myRecipeUiState.title)},
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        },
        actions = {
            IconButton(onClick = { navigateToEditMyRecipe(myRecipeUiState.id) }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Recipe"
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_button)
                )
            }
        }
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


@Composable
private fun SummaryScreen(
    myRecipeUiState: MyRecipeUiState,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(myRecipeUiState.image)
                    .crossfade(true)
                    .build(),
                contentDescription = myRecipeUiState.title,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img),
                contentScale = ContentScale.FillWidth
            )
        }
        Text(text = myRecipeUiState.title)
        Text(text = stringResource(id = R.string.cook_time))
        Text(text = myRecipeUiState.readyInMinutes.toString())
        Text(text = stringResource(id = R.string.servings))
        Text(text = myRecipeUiState.servings.toString())
        Text(text = stringResource(id = R.string.description))
        Text(text = myRecipeUiState.summary)
    }
}

@Composable
private fun IngredientsScreen(
    myRecipeUiState: MyRecipeUiState,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        var i = 1
        myRecipeUiState.ingredient.lines().forEach{ item ->
            Text(text = "$i) $item")
            i += 1
        }
    }
}

@Composable
private fun InstructionsScreen(
    myRecipeUiState: MyRecipeUiState,
    tabsViewModel: TabsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        var i = 1
        myRecipeUiState.instructions.lines().forEach{ items ->
            Text(text = "$i) $items")
            i += 1
        }
    }
}
