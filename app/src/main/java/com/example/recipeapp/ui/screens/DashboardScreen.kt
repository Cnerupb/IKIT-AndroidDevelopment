package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipeapp.ui.navigation.Routes

@Composable
fun DashboardScreen(navController: NavController, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("DashboardScreen")
    }
}