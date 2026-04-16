package com.example.recipeapp.data

data class Recipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val cookingTimeMinutes: Int,
    val calories: Int,
    val proteins: Float,
    val fats: Float,
    val carbohydrates: Float,
    val ingredients: List<String>,
    val steps: List<String>
)

val sampleRecipes = listOf(
    Recipe(
        id = 1,
        name = "Паста Карбонара",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/3/33/Fresh_made_pasta_carbonara.jpg",
        cookingTimeMinutes = 30,
        calories = 380,
        proteins = 15.2f,
        fats = 18.4f,
        carbohydrates = 41.6f,
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
        proteins = 4.8f,
        fats = 12.1f,
        carbohydrates = 7.9f,
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