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
import androidx.navigation.navigation
import com.kiselev.financialcompanion.view.BudgetScreen
import com.kiselev.financialcompanion.view.GraphScreen
import com.kiselev.financialcompanion.view.LoginScreen
import com.kiselev.financialcompanion.view.OperationAdd
import com.kiselev.financialcompanion.view.OperationScreen
import com.kiselev.financialcompanion.view.ProfileAdd
import com.kiselev.financialcompanion.view.ProfileView
import com.kiselev.financialcompanion.view.RegistrationScreen
import com.kiselev.financialcompanion.view.WelcomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Welcome"
    ) {
        composable(route = "Welcome") { WelcomeScreen(navController) }
        composable(route = "Login") { LoginScreen(viewModel(), navController) }
        composable(route = "Registration") { RegistrationScreen(viewModel(), navController) }
        composable(route = "MainScreen") { MainScreen(navController) }
        composable(route = "ProfileAdd") { ProfileAdd(viewModel(), navController)}
        composable(route = "OperationAdd") { OperationAdd(viewModel(), navController) }
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
        composable(route = "Profile") { ProfileView(viewModel = viewModel(), mainController) }
    }
}