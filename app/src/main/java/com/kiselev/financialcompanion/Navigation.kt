package com.kiselev.financialcompanion

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.kiselev.financialcompanion.screens.BudgetScreen
import com.kiselev.financialcompanion.screens.GraphScreen
import com.kiselev.financialcompanion.screens.Operation
import com.kiselev.financialcompanion.screens.TransactionAdd

fun NavGraphBuilder.BottomNavGraph(route: String, appState: MultiNavigationAppState) {
    navigation(startDestination = appState.getStartDestination, route = route){
        composable(route = "Operation") {Operation()}
        composable(route = "Budget") {BudgetScreen()}
        composable(route = "Graph") {GraphScreen()}
    }
}

fun NavGraphBuilder.OtherNavGraph(route: String, appState: MultiNavigationAppState){
    navigation(startDestination = appState.getStartDestination, route = route){
        composable( route = "TransactionAdd") { TransactionAdd() }
    }
}
