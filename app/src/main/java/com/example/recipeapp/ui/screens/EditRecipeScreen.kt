package com.example.recipeapp.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.components.EditableListItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.ui.viewmodel.RecipeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(navController: NavController, recipeViewModel: RecipeViewModel, recipeId: Long) {
    val recipeState by recipeViewModel.selectedRecipe.collectAsState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(recipeId) {
        recipeViewModel.getRecipeById(recipeId)
    }

    val recipe = recipeState ?: return

    var name by remember(recipe) { mutableStateOf(recipe.name) }
    var imageUrl by remember(recipe) { mutableStateOf(recipe.imageUrl ?: "") }
    var cookingTime by remember(recipe) { mutableStateOf(recipe.cookingTimeMinutes.toString()) }
    var calories by remember(recipe) { mutableStateOf(recipe.calories.toString()) }
    var proteins by remember(recipe) { mutableStateOf(recipe.proteins.toString()) }
    var fats by remember(recipe) { mutableStateOf(recipe.fats.toString()) }
    var carbohydrates by remember(recipe) { mutableStateOf(recipe.carbohydrates.toString()) }
    val ingredients = remember(recipe) { mutableStateListOf(*recipe.ingredients.toTypedArray()) }
    val steps = remember(recipe) { mutableStateListOf(*recipe.steps.toTypedArray()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить рецепт?") },
            text = { Text("Это действие нельзя отменить.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    scope.launch {
                        recipeViewModel.delete(recipe)
                        navController.popBackStack()
                        navController.popBackStack() // Go back to list
                    }
                }) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование") },
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
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
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
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = true
                    )
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

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = { showDeleteDialog = true },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Icon(Icons.Default.Delete, "Удалить рецепт")
                }
                FloatingActionButton(onClick = {
                    val updatedRecipe = recipe.copy(
                        name = name,
                        imageUrl = imageUrl.ifBlank { null },
                        cookingTimeMinutes = cookingTime.toIntOrNull() ?: 0,
                        calories = calories.toIntOrNull() ?: 0,
                        proteins = proteins.replace(',', '.').toDoubleOrNull() ?: 0.0,
                        fats = fats.replace(',', '.').toDoubleOrNull() ?: 0.0,
                        carbohydrates = carbohydrates.replace(',', '.').toDoubleOrNull() ?: 0.0,
                        ingredients = ingredients.filter { it.isNotBlank() },
                        steps = steps.filter { it.isNotBlank() }
                    )
                    scope.launch {
                        recipeViewModel.update(updatedRecipe)
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.Default.Save, "Сохранить")
                }
            }
        }
    }
}
