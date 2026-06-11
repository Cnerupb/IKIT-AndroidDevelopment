package com.example.recipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageUrl: String? = null,
    val cookingTimeMinutes: Int,
    val calories: Int,
    val proteins: Double,
    val fats: Double,
    val carbohydrates: Double,
    val ingredients: List<String>,
    val steps: List<String>
)

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}

val sampleRecipes = listOf(
    Recipe(
        id = 1,
        name = "Паста Карбонара",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/33/Fresh_made_pasta_carbonara.jpg",
        cookingTimeMinutes = 30,
        calories = 380,
        proteins = 15.0,
        fats = 18.0,
        carbohydrates = 42.0,
        ingredients = listOf(
            "Спагетти — 200 г",
            "Бекон — 100 г",
            "Яйца — 2 шт",
            "Пармезан — 50 г",
            "Чёрный перец — по вкусу",
            "Соль — по вкусу"
        ),
        steps = listOf(
            "Отварите спагетти до состояния аль денте в подсоленной воде.",
            "Обжарьте бекон на сухой сковороде до хрустящей корочки.",
            "Смешайте яйца с натёртым пармезаном и чёрным перцем.",
            "Соедините горячие спагетти с беконом, снимите с огня.",
            "Добавьте яично-сырную смесь и быстро перемешайте.",
            "Подавайте немедленно, посыпав пармезаном и перцем."
        )
    ),
    Recipe(
        id = 2,
        name = "Греческий салат",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/76/Greek_salad.jpg",
        cookingTimeMinutes = 15,
        calories = 160,
        proteins = 5.0,
        fats = 12.0,
        carbohydrates = 8.0,
        ingredients = listOf(
            "Помидоры — 3 шт",
            "Огурцы — 2 шт",
            "Перец болгарский — 1 шт",
            "Лук красный — 0.5 шт",
            "Маслины — 50 г",
            "Сыр фета — 150 г",
            "Оливковое масло — 3 ст. л.",
            "Орегано — по вкусу"
        ),
        steps = listOf(
            "Нарежьте помидоры, огурцы и перец крупными кусками.",
            "Нарежьте лук тонкими полукольцами.",
            "Смешайте все овощи в глубоком салатнике.",
            "Добавьте маслины и нарезанную кубиками фету.",
            "Полейте оливковым маслом и посыпьте орегано."
        )
    )
)
