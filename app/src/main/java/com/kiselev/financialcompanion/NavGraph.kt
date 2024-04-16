package com.kiselev.financialcompanion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.screens.BudgetScreen
import com.kiselev.financialcompanion.screens.GraphScreen
import com.kiselev.financialcompanion.screens.LoginScreen
import com.kiselev.financialcompanion.screens.OperationAdd
import com.kiselev.financialcompanion.screens.OperationScreen
import com.kiselev.financialcompanion.screens.RegistrationScreen
import com.kiselev.financialcompanion.screens.WelcomeScreen

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Welcome"
    ) {
        composable(route = "Welcome") { WelcomeScreen(navController) }
        composable(route = "Login") { LoginScreen(viewModel = viewModel(), navController) }
        composable(route = "Registration") { RegistrationScreen(viewModel = viewModel(), navController) }
        composable(route = "MainNavGraph") { MainNavGraph()}
    }
}

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "BottomNavGraph"
    ) {
        composable(route = "BottomNavGraph") { MainScreen(navController) }
        composable(route = "OtherNavGraph") { OtherNavGraph() }
    }
}

@Composable
fun BottomNavGraph(navController: NavController, mainController: NavHostController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "Operation"
    ) {
        composable(route = "Operation") { OperationScreen(viewModel = viewModel(), mainController) }
        composable(route = "Budget") { BudgetScreen() }
        composable(route = "Graph") { GraphScreen() }
        composable(route = "Profile") { GraphScreen() }
    }
}

@Composable
fun OtherNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "OperationAdd"
    ) {
        composable(route = "OperationAdd") { OperationAdd() }
    }
}