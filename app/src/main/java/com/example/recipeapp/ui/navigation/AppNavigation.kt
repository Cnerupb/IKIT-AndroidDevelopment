package com.example.recipeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeapp.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.DASHBOARD) {
            DrawerScaffold(
                navController = navController,
                currentRoute = Routes.DASHBOARD,
                title = "Дашборд"
            ) { padding ->
                DashboardScreen(navController, padding)
            }
        }
        composable(Routes.MY_RECIPES) {
            DrawerScaffold(
                navController = navController,
                currentRoute = Routes.MY_RECIPES,
                title = "Мои рецепты",
                floatingActionButton = {
                    FloatingActionButton(onClick = { navController.navigate(Routes.CREATE_RECIPE) }) {
                        Icon(Icons.Default.Add, contentDescription = "Создать рецепт")
                    }
                },
                fabPosition = FabPosition.Center
            ) { padding ->
                MyRecipesScreen(navController, padding)
            }
        }
        composable(Routes.ABOUT) {
            DrawerScaffold(
                navController = navController,
                currentRoute = Routes.ABOUT,
                title = "О нас"
            ) { padding ->
                AboutScreen(navController, padding)
            }
        }

        // Экраны без drawer — с обычным TopAppBar и стрелкой "Назад"
        composable(Routes.CREATE_RECIPE) {
            CreateRecipeScreen(navController)
        }
        composable(
            route = Routes.RECIPE_DETAIL,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            RecipeDetailScreen(navController, recipeId)
        }
        composable(
            route = Routes.EDIT_RECIPE,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            EditRecipeScreen(navController, recipeId)
        }
    }
}