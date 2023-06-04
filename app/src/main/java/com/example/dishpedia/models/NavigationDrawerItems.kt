package com.example.dishpedia.models

class NavigationDrawerItems (
    val name: String,
    val route: String,
)

object NavigationDrawerItemsProvider{
    val navItems = listOf(
        NavigationDrawerItems(
            name = "Home",
            route = "Home",
        ),
        NavigationDrawerItems(
            name = "My Recipes",
            route = "MyRecipes",
        ),
        //TODO: Add authorization, then accounts
//        NavigationDrawerItems(
//            name = "Account",
//            route = "Account",
//        )
    )
}
