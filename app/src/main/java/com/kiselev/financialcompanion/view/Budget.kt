package com.kiselev.financialcompanion.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kiselev.financialcompanion.controller.BudgetController
import com.kiselev.financialcompanion.model.Budget
import com.kiselev.financialcompanion.model.getCategoryIcon
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor3
import com.kiselev.financialcompanion.ui.theme.primaryColor
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetScreen(viewModel: BudgetController, navController: NavController){
    val context = LocalContext.current
    var budgets by remember { mutableStateOf<List<Budget>?>(null) }
    LaunchedEffect(key1 = Unit) {
        budgets = viewModel.getBudgets(context)
        println("Полученные бюджеты = $budgets")
    }

    Scaffold(
        modifier = Modifier.background(grayColor2),
        topBar = { TopAppBar() },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = primaryColor,
                onClick = {
                    navController.navigate("BudgetAdd") {}
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        },
        content = { paddingValues ->
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues))
            {
                budgets?.let { BudgetView(it) }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BudgetItem(name: String, amount: String, type: Int, startDate: String, endDate: String){
    val startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val days = ChronoUnit.DAYS.between(startDateTime, endDateTime)

    val formattedEndDate = endDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    val dayOfWeek = getDayOfWeek(endDateTime.dayOfWeek.value)
    val categoryIcon = getCategoryIcon("Транспорт")

    Spacer(
        modifier = Modifier.height(16.dp)
    )
    Column (
        modifier = Modifier
            .background(Color.White)
    ){
        Row (
            modifier = Modifier.padding(top = 4.dp)
        ){
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .align(Alignment.CenterVertically),
                text = days.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily
            )
            Text(
                text = "до $dayOfWeek\n${formattedEndDate} г.",
                color = Color.Black,
                style = TextStyle(letterSpacing = 0.sp),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily
            )
            Text(
                text = name,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = InterFamily
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            thickness = 1.dp,
            color = grayColor2
        )
        Column {
            Row() {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = "10 операций",
                    color = grayColor3,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = InterFamily
                )
                Text(
                    text = "10000",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = InterFamily
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    text = "/",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = InterFamily
                )
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = amount,
                    color = primaryColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = InterFamily
                )
            }
            CustomProgressBar(progress = 14000, amount = amount.toInt())
        }
    }
}

private fun getDayOfWeek(dayOfWeek: Int): String {
    val weekdays = DateFormatSymbols().weekdays
    return weekdays[(dayOfWeek % 7) + 1]
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun BudgetView(budgets: List<Budget>) {
    LazyColumn(
        modifier = Modifier
            .background(grayColor2)
    ) {
        items(items = budgets) { budget ->
            BudgetItem(
                name = budget.name,
                amount = budget.amount.toString(),
                type = budget.type,
                startDate = budget.start_date,
                endDate = budget.end_date
            )
        }
    }
}

@Composable
fun CustomProgressBar(progress: Int, amount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .height(20.dp)
                .background(grayColor)
                .width(4100.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0F9D58),
                                Color(0xF055CA4D)
                            )
                        )
                    )
                    .width(363.dp * progress / amount)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BudgetScreenPreview(){
    BudgetItem(name = "Продукты", amount = "25000", type = 1, startDate = "2024-05-01", endDate= "2024-06-01")
}