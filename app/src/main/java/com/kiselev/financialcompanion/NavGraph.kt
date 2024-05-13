package com.kiselev.financialcompanion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.view.AccountListScreen
import com.kiselev.financialcompanion.view.BudgetAdd
import com.kiselev.financialcompanion.view.BudgetScreen
import com.kiselev.financialcompanion.view.GraphScreen
import com.kiselev.financialcompanion.view.LoginScreen
import com.kiselev.financialcompanion.view.OperationAdd
import com.kiselev.financialcompanion.view.OperationScreen
import com.kiselev.financialcompanion.view.ProfileAdd
import com.kiselev.financialcompanion.view.ProfileView
import com.kiselev.financialcompanion.view.RegistrationScreen
import com.kiselev.financialcompanion.view.SelectionListScreen
import com.kiselev.financialcompanion.view.WelcomeScreen

@Composable
fun MainNavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "Welcome") { WelcomeScreen(navController) }
        composable(route = "Login") { LoginScreen(viewModel(), navController) }
        composable(route = "Registration") { RegistrationScreen(viewModel(), navController) }

        composable(route = "MainScreen") { MainScreen(navController) }

        composable(route = "OperationAdd") { entry ->
            OperationAdd(viewModel(), navController, entry.savedStateHandle.get<String>("category"), entry.savedStateHandle.get<String>("account_name"), entry.savedStateHandle.get<String>("account_id")) }
        composable(route = "BudgetAdd") { entry ->
            BudgetAdd(viewModel(), navController, entry.savedStateHandle.get<String>("category")) }
        composable(route = "ProfileAdd") { ProfileAdd(viewModel(), navController)}
        composable(route = "SelectionList") { SelectionListScreen(navController) }
        composable(route = "AccountList") { AccountListScreen(viewModel(), navController) }
    }
}

@Composable
fun BottomNavGraph(navController: NavController, mainController: NavHostController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "Operation"
    ) {
        composable(route = "Operation") { OperationScreen(viewModel(), mainController) }
        composable(route = "Budget") { BudgetScreen(viewModel(), mainController) }
        composable(route = "Graph") { GraphScreen(viewModel()) }
        composable(route = "Profile") { ProfileView(viewModel(), mainController) }
    }
}