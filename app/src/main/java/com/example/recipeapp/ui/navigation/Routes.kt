package com.example.recipeapp.ui.navigation

object Routes {
    // Экраны внутри drawer
    const val DASHBOARD = "dashboard"
    const val MY_RECIPES = "my_recipes"
    const val ABOUT = "about"

    // Экраны вне drawer
    const val CREATE_RECIPE = "create_recipe"
    const val RECIPE_DETAIL = "recipe_detail/{recipeId}"
    const val EDIT_RECIPE = "edit_recipe/{recipeId}"

    // Хелперы для подстановки параметров
    fun recipeDetail(id: Int) = "recipe_detail/$id"
    fun editRecipe(id: Int) = "edit_recipe/$id"
}