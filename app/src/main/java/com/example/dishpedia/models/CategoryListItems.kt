package com.example.dishpedia.models

import androidx.annotation.DrawableRes
import com.example.dishpedia.R

data class CategoryListItems (
    @DrawableRes val image: Int,
    val text: String,
    val query: String
)

object CategoryListItemsProvider{
    val mainCourse =
        CategoryListItems(
            image = R.drawable.maincourse,
            text = "Main Course",
            query = "main course"
        )
    val bread =
        CategoryListItems(
            image = R.drawable.bread,
            text = "Breads",
            query = "bread"
        )
    val appetizer =
        CategoryListItems(
            image = R.drawable.appetizer,
            text = "Appetizers",
            query = "appetizer"
        )
    val beverage =
        CategoryListItems(
            image = R.drawable.beverage,
            text = "Beverages",
            query = "beverage"
        )
    val breakfast =
        CategoryListItems(
            image = R.drawable.breakfast,
            text = "Breakfast",
            query = "breakfast"
        )
    val dessert =
        CategoryListItems(
            image = R.drawable.desserts,
            text = "Desserts",
            query = "dessert"
        )
    val salad =
        CategoryListItems(
            image = R.drawable.salad,
            text = "Salads",
            query = "salad"
        )
    val sideDish =
        CategoryListItems(
            image = R.drawable.sidedish,
            text = "Side Dishes",
            query = "side dish"
        )
    val snacks =
        CategoryListItems(
            image = R.drawable.snacks,
            text = "Snacks",
            query = "snack"
        )
    val soup =
        CategoryListItems(
            image = R.drawable.soup,
            text = "Soups",
            query = "soup"
        )
}