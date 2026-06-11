package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.EditableListItem

import androidx.compose.runtime.rememberCoroutineScope
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.ui.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(navController: NavController, recipeViewModel: RecipeViewModel) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var cookingTime by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var proteins by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbohydrates by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf("") }
    val steps = remember { mutableStateListOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новый рецепт") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL изображения") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = cookingTime,
                        onValueChange = { cookingTime = it },
                        label = { Text("Время (мин)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Калории (ккал)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = proteins,
                        onValueChange = { proteins = it },
                        label = { Text("Белки (г)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it },
                        label = { Text("Жиры (г)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    OutlinedTextField(
                        value = carbohydrates,
                        onValueChange = { carbohydrates = it },
                        label = { Text("Углеводы (г)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                Text("Ингредиенты", style = MaterialTheme.typography.titleMedium)
                ingredients.forEachIndexed { index, item ->
                    EditableListItem(
                        value = item,
                        onValueChange = { ingredients[index] = it },
                        onDelete = { ingredients.removeAt(index) },
                        onMoveUp = {
                            val removed = ingredients.removeAt(index)
                            ingredients.add(index - 1, removed)
                        },
                        onMoveDown = {
                            val removed = ingredients.removeAt(index)
                            ingredients.add(index + 1, removed)
                        },
                        canMoveUp = index > 0,
                        canMoveDown = index < ingredients.size - 1
                    )
                }
                OutlinedButton(
                    onClick = { ingredients.add("") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Добавить ингредиент")
                }

                Text("Пошаговое описание", style = MaterialTheme.typography.titleMedium)
                steps.forEachIndexed { index, step ->
                    EditableListItem(
                        value = step,
                        onValueChange = { steps[index] = it },
                        onDelete = { steps.removeAt(index) },
                        onMoveUp = {
                            val removed = steps.removeAt(index)
                            steps.add(index - 1, removed)
                        },
                        onMoveDown = {
                            val removed = steps.removeAt(index)
                            steps.add(index + 1, removed)
                        },
                        canMoveUp = index > 0,
                        canMoveDown = index < steps.size - 1,
                        label = "${index + 1}."
                    )
                }
                OutlinedButton(
                    onClick = { steps.add("") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Добавить шаг")
                }
            }

            FloatingActionButton(
                onClick = {
                    val recipe = Recipe(
                        name = name,
                        imageUrl = imageUrl.ifBlank { null },
                        cookingTimeMinutes = cookingTime.toIntOrNull() ?: 0,
                        calories = calories.toIntOrNull() ?: 0,
                        proteins = proteins.toDoubleOrNull() ?: 0.0,
                        fats = fats.toDoubleOrNull() ?: 0.0,
                        carbohydrates = carbohydrates.toDoubleOrNull() ?: 0.0,
                        ingredients = ingredients.filter { it.isNotBlank() },
                        steps = steps.filter { it.isNotBlank() }
                    )
                    scope.launch {
                        recipeViewModel.insert(recipe)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Save, "Сохранить")
            }
        }
    }
}