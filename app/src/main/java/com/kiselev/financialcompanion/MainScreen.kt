package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainController: NavHostController) {
    val navController = rememberNavController()
     Scaffold (
         bottomBar = { BottomNavigation(navController = navController) }
     ){ innerPadding ->
         Box(modifier = Modifier.padding(innerPadding)) {
             BottomNavGraph(navController = navController, mainController = mainController)
         }
     }
}

@Composable
fun BottomNavigation(navController: NavController){
    val listItems = listOf(
        NavigationItem.Operation,
        NavigationItem.Budget,
        NavigationItem.Graph
    )
    NavigationBar(
        containerColor = Color.White
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { item->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(route = item.route)
                { launchSingleTop = true }
                   },
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = "BottomIcon") },
                label = { Text(text= item.title, fontSize = 13.sp) },
                colors = NavigationBarItemDefaults.colors(selectedTextColor = Red, unselectedIconColor = Gray)
            )
        }
    }
}