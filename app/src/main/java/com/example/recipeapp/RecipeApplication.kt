package com.example.recipeapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class RecipeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
    }
}
