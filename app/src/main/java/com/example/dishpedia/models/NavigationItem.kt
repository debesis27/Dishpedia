package com.example.dishpedia.models

interface NavigationItem {
    val name: String
    val route: String
}

object NavigationItemsProvider{
    object Home : NavigationItem {
        override val name = "Home"
        override val route = "home"
    }
    object MyRecipes : NavigationItem {
        override val name = "My Recipes"
        override val route = "my_recipes"
    }
    //TODO: Add authorization, then accounts
    object Account : NavigationItem{
        override val name = "Account"
        override val  route = "Account"
    }
    object Search : NavigationItem{
        override val name = "Search"
         override val route = "search"
    }
    //Shows info of recipes that are obtained from api call
    object recipeInfo : NavigationItem{
        override val name = "Recipe Information"
        override val route = "recipe_info"
    }
    //Shows info of recipes that are stored in local database
    object MyRecipeDetails : NavigationItem{
        override val name = "Recipe Details"
        override val route = "recipe_details"
        const val recipeIdArg = "recipeId"
        val routeWithArgs = "$route/{$recipeIdArg}"
    }
    object MyRecipeEntry : NavigationItem{
        override val name = "Add a new Recipe"
        override val route = "recipe_entry"
    }
    object MyRecipeEdit : NavigationItem{
        override val name = "Edit Recipe"
        override val route = "recipe_edit"
        const val recipeIdArg = "itemId"
        val routeWithArgs = "$route/{$recipeIdArg}"
    }

}

object NavigationDrawerItemsProvider{
    val navItems = listOf(
        NavigationItemsProvider.Home,
        NavigationItemsProvider.MyRecipes
    )
}
