package com.example.dishpedia.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import com.example.dishpedia.R
import com.example.dishpedia.models.NavigationItemsProvider
import com.example.dishpedia.utils.MyRecipeEditBody
import com.example.dishpedia.viewmodel.AppViewModelProvider
import com.example.dishpedia.viewmodel.MyRecipeEntryViewModel
import kotlinx.coroutines.launch

@Composable
fun MyRecipeEntryScreen(
    context: Context,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    myRecipeEntryViewModel: MyRecipeEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MyRecipeEntryScreenTopBar(
                title = NavigationItemsProvider.MyRecipeEntry.name,
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) {
        MyRecipeEditBody(
            context = context,
            myRecipeUiState = myRecipeEntryViewModel.recipeUiState,
            onRecipeValueChange = myRecipeEntryViewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    myRecipeEntryViewModel.saveRecipe()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun MyRecipeEntryScreenTopBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    if(canNavigateBack){
        TopAppBar(
            title = { Text(text = title) },
            modifier = modifier,
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
