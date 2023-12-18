package com.kiselev.financialcompanion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.BASE_ROUTE
import com.kiselev.financialcompanion.BottomNavGraph
import com.kiselev.financialcompanion.LocalNavigationState
import com.kiselev.financialcompanion.MultiNavigationStates
import com.kiselev.financialcompanion.NavigationItem
import com.kiselev.financialcompanion.OtherItem
import com.kiselev.financialcompanion.OtherNavGraph
import com.kiselev.financialcompanion.PRODUCT_ROUTE
import com.kiselev.financialcompanion.ROOT_ROUTE
import com.kiselev.financialcompanion.rememberMultiNavigationAppState
import com.kiselev.financialcompanion.ui.theme.primaryColor

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
     Scaffold (
         modifier = Modifier.fillMaxSize(),
         topBar = { TopAppBar() },
         bottomBar = { BottomNavigation(navController = navController) }
     ){
             innerPadding ->
         Box(modifier = Modifier.padding(innerPadding)) {
             BottomNavGraph(navController = navController)
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
                onClick = { navController.navigate(route = item.route)
                          },
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = "BottomIcon") },
                label = { Text(text= item.title, fontSize = 13.sp) },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Gray,
                    unselectedIconColor = Gray,
                    selectedTextColor = primaryColor,
                    selectedIconColor = primaryColor,
                    indicatorColor = Red
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = primaryColor
        ),
        title = {
            Text("Финансовый спутник", color= Color.White)
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Menu, tint=Color.White, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = { }){
                Icon(imageVector = Icons.Filled.MoreVert, tint=Color.White, contentDescription = "")
            }
        }
    )
}

@Composable
fun ContentWrapper() {
    LocalNavigationState = MultiNavigationStates(
        rootNavigation = rememberMultiNavigationAppState(startDestination = ROOT_ROUTE),

        baseNavigation = rememberMultiNavigationAppState(startDestination = NavigationItem.Operation.route),
        productNavigation = rememberMultiNavigationAppState(startDestination = OtherItem.TransactionAdd.route)
    )
    NavHost(
        navController = LocalNavigationState.rootNavigation.getNavController,
        startDestination = BASE_ROUTE,
        route = ROOT_ROUTE
    ) {
        composable(
            route = BASE_ROUTE,
        ) {
            LocalNavigationState.baseNavigation.setNavController(rememberNavController())
            Dashboard {
                NavHost(
                    navController = LocalNavigationState.baseNavigation.getNavController,
                    startDestination = BASE_ROUTE,
                    route = ROOT_ROUTE
                ) {
                    BottomNavGraph(
                        appState = LocalNavigationState.baseNavigation,
                        route = BASE_ROUTE,
                    )
                }
            }
        }
        composable(
            route = PRODUCT_ROUTE,
        ) {
            LocalNavigationState.productNavigation.setNavController(rememberNavController())
            Box() {
                NavHost(
                    navController = LocalNavigationState.productNavigation.getNavController,
                    startDestination = PRODUCT_ROUTE,
                    route = ROOT_ROUTE
                ) {
                    OtherNavGraph(
                        appState = LocalNavigationState.productNavigation,
                        route = PRODUCT_ROUTE,
                    )
                }
            }
        }
    }
}
