package com.example.dishpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dishpedia.ui.theme.DishpediaTheme
import com.example.dishpedia.viewmodel.RecipesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DishpediaTheme {
                val recipeViewModel : RecipesViewModel =
                    viewModel(factory = RecipesViewModel.Factory)
                DishpediaApp(recipeViewModel)
            }
        }
    }
}
