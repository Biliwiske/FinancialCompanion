package com.kiselev.financialcompanion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@RequiresApi(Build.VERSION_CODES.O)
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
            OperationAdd(viewModel(), navController, entry.savedStateHandle.get<String>("category")) }
        composable(route = "BudgetAdd") { BudgetAdd(viewModel(), navController) }
        composable(route = "ProfileAdd") { ProfileAdd(viewModel(), navController)}
        composable(route = "SelectionList") { SelectionListScreen(viewModel(), navController) }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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