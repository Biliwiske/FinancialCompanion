package com.kiselev.financialcompanion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.controller.OperationViewController
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.model.getCategoryIcon
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor3
import com.kiselev.financialcompanion.ui.theme.primaryColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OperationScreen(viewModel: OperationViewController, navController: NavController) {
    val context = LocalContext.current
    var transactions by remember { mutableStateOf<List<Transaction>?>(null) }
    LaunchedEffect(key1 = Unit) {
        transactions = viewModel.getTransactions(context)
        transactions = transactions?.sortedByDescending { it.date }
        println("Первый тест $transactions")
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
                val dailyTotals = transactions?.let { it1 -> viewModel.calculateTransactions(it1) }
                transactions?.let { RecyclerView(it, dailyTotals) }
            }
        }
    )
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
fun ListItem(date: String, name: String, cost: String, type: Int, account: String, category: String){
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val parsedDate = dateFormat.parse(date)
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(parsedDate!!)
    val expanded = remember { mutableStateOf(false)}
    var amount = cost
    val transactionColor = if(type == 0){
        primaryColor
    }else{
        amount = "$cost"
        Color.Red
    }
    val categoryIcon = getCategoryIcon(category)

    HorizontalDivider(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        color = grayColor2,
        thickness = 1.dp
    )
    Surface(
        color = Color.White,
    ) {
        Column(modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()) {
            Row {
                Image(
                    painter = painterResource(id = categoryIcon),
                    contentDescription = "Company logo",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                        .size(width = 40.dp, height = 35.dp))
                Column (modifier = Modifier.weight(1f)){
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = InterFamily,
                        style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = account,
                        color = grayColor3,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily)
                }
                Column {
                    Text(
                        text = "$amount ₽",
                        color = transactionColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = InterFamily)
                    Text(
                        text = time,
                        modifier = Modifier.align(Alignment.End),
                        color = grayColor3,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily)
                }
            }
        }
    }
}

@Composable
fun RecyclerView(transactions: List<Transaction>, dailyTotals: Map<Date?, Int>?, ) {
    var prevDate: Date? = null // Переменная для хранения предыдущей даты
    LazyColumn(
        modifier = Modifier
            .background(grayColor2)
    ) {
        items(items = transactions) { transaction ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate = dateFormat.parse(transaction.date)
            val dates = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsedDate!!)

            if (parsedDate != prevDate) {
                val total = dailyTotals?.get(parsedDate) ?: 0
                DateHeader(date = dates, amount = total)
            }
            prevDate = parsedDate

            ListItem(
                name = transaction.description,
                date = transaction.date,
                cost = transaction.amount.toString(),
                type = transaction.type,
                account = transaction.account_name,
                category = transaction.category
            )
        }
    }
}

@Composable
fun DateHeader(date: String, amount: Int) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = dateFormat.parse(date)
    val dates = SimpleDateFormat("d", Locale.getDefault()).format(parsedDate!!)
    val formattedDate = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(parsedDate)
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(parsedDate)
    var value = ""
    if(amount < 0){
        value = (amount * -1).toString()
        value = "- $value"
    }
    HorizontalDivider(
        color = grayColor2,
        thickness = 18.dp
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp),
            text = dates,
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily
        )
        Text(
            text = "$dayOfWeek\n$formattedDate г.",
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
            text = "$value ₽",
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily
        )
    }
    HorizontalDivider(
        color = grayColor2,
        thickness = 2.dp
    )
}

@Preview(showBackground = true)
@Composable
fun DateHeaderTest() {
    DateHeader(date = "2024-03-29 14:16:17", 10000)
}

@Preview(showBackground = true)
@Composable
fun ListTest() {
    ListItem(date = "2024-03-29 14:16:17", "Кожаная куртка", "10000", 1, "Наличные", "Транспорт")
}

@Preview(showBackground = true)
@Composable
fun Test() {
    //OperationScreen(viewModel = viewModel(), navController = rememberNavController())
}