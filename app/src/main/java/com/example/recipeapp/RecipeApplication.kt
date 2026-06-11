package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.RecipeRepository
import com.yandex.mapkit.MapKitFactory

class RecipeApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { RecipeRepository(database.recipeDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}
