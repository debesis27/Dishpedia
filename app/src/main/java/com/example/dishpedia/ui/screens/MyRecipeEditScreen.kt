package com.example.dishpedia.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dishpedia.R
import com.example.dishpedia.utils.MyRecipeEditBody
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.MyRecipeEditViewModel
import kotlinx.coroutines.launch

@Composable
fun MyRecipeEditScreen(
    context: Context,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    myRecipeEditViewModel: MyRecipeEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val myRecipeUiState = myRecipeEditViewModel.recipeUiState

    Scaffold(
        topBar = {
            MyRecipeEditScreenTopBar(
                title = myRecipeUiState.title,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        MyRecipeEditBody(
            context = context,
            myRecipeUiState = myRecipeUiState,
            onRecipeValueChange = myRecipeEditViewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    myRecipeEditViewModel.updateRecipe()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MyRecipeEditScreenTopBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    if(canNavigateBack){
        TopAppBar(
            title = { Text(text = title) },
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.surface,
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        )
    }else{
        TopAppBar(
            title = { Text(text = title) },
            modifier = modifier
        )
    }
}