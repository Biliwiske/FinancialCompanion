package com.kiselev.financialcompanion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.ui.theme.primaryColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainController: NavHostController) {
    val navController = rememberNavController()
     Scaffold (
         bottomBar = { BottomNavigation(navController = navController) }
     )
     { innerPadding ->
         Box(modifier = Modifier.padding(innerPadding)) {
             BottomNavGraph(navController = navController, mainController = mainController)
         }
     }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val listItems = listOf(
        NavigationItem.Operation,
        NavigationItem.Budget,
        NavigationItem.Graph
    )
    var selectedItem = 0
    NavigationBar(
        containerColor = Color.White
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    selectedItem = item.number
                    navController.navigate(route = item.route) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = "BottomIcon",
                        tint = if(selectedItem == item.number) primaryColor else Color.Black
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if(selectedItem == item.number) primaryColor else Color.Black,
                        fontSize = 12.sp) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    MainScreen(mainController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun Preview2(){
    BottomNavigation(navController = rememberNavController())
}
