package com.kiselev.financialcompanion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kiselev.financialcompanion.screens.BudgetScreen
import com.kiselev.financialcompanion.screens.GraphScreen
import com.kiselev.financialcompanion.screens.OperationScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Operation"
    ) {
        composable(route = "Operation") {
            OperationScreen()
        }
        composable(route = "Budget") {
            BudgetScreen()
        }
        composable(route = "Graph") {
            GraphScreen()
        }
    }
}