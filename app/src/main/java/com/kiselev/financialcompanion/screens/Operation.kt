package com.kiselev.financialcompanion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.model.TransactionApi
import com.kiselev.financialcompanion.ui.theme.primaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OperationScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar() },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = primaryColor,
                onClick = {
                    navController.navigate("OtherNavGraph") {}
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it))
            {
                """
                try{
                    
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.1.30/financial-companion-server/")
                        .addConverterFactory(GsonConverterFactory.create()).build()
                    val transactionApi = retrofit.create(TransactionApi::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val transactions: List<Transaction> = transactionApi.getTransactions()
                        println(transactions)
                    }
                    
                } catch (e: Exception){
                    e.printStackTrace()
                }
                """
                RecyclerView()
            }
        })
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
fun ListItem(date: String, name: String, cost: String){
    val expanded = remember { mutableStateOf(false)}

    Surface(
        color = Color.White,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()) {
            Row {
                Column(modifier = Modifier.weight(1f)) {

                    Text(text = name, fontSize = 18.sp)
                    Text(text = date)
                }
                Text(text = cost, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun RecyclerView(names : List<String> = List(10){"$it"}){
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
        items(items = names){name ->
            ListItem(name = name, date = "12.02.2023", cost = "12.000")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Test(){
    RecyclerView()
}