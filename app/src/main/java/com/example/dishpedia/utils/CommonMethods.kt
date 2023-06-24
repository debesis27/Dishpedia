package com.example.dishpedia.utils

import com.example.dishpedia.models.Nutrients

fun showNutritionCard(nutrients: List<Nutrients>) : Boolean {
    var availableNutrients = 0
    nutrients.forEach { nutrient ->
        if(nutrient.amount != -1.0) availableNutrients += 1
    }

    return availableNutrients != 0
}