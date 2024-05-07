package com.kiselev.financialcompanion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.controller.ProfileController
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.getCategoryIcon
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.primaryColor

@Composable
fun ProfileView(viewModel: ProfileController, navController: NavController) {
    val context = LocalContext.current
    var accounts by remember { mutableStateOf<List<Account>?>(null) }
    LaunchedEffect(key1 = Unit) {
        accounts = viewModel.getAccounts(context)
        println("Счета: $accounts")
    }

    Scaffold(
        modifier = Modifier.background(grayColor2),
        topBar = { TopAppBar() },
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it))
            {
                Column(
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                ) {
                    accounts?.let { RecyclerView(it) }
                    Text(
                        text = "Добавить счет",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable { navController.navigate("ProfileAdd") },
                        color = primaryColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = InterFamily,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.logout(navController, context)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Выйти из аккаунта",
                            fontWeight = FontWeight.Medium,
                            fontFamily = InterFamily)
                    }
                }
            }
        }
    )
}

@Composable
fun ListItem(accountName: String, accountBalance: Int, accountCurrency: String){
    val categoryIcon = getCategoryIcon("Транспорт")

    Row(
        modifier = Modifier
            .padding(6.dp)
            .background(Color.White)
    ){
        Image(
            painter = painterResource(id = categoryIcon),
            contentDescription = "Company logo",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp)
                .size(width = 25.dp, height = 25.dp))
        Text(
            text = accountName,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium)
        Text(
            text = accountBalance.toString(),
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 19.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun RecyclerView(accounts: List<Account>) {
    LazyColumn(
        modifier = Modifier
            .background(grayColor2)
    ) {
        items(items = accounts) { account ->
            ListItem(
                accountName = account.name,
                accountBalance = account.balance,
                accountCurrency = account.currency
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview(){
    ProfileView(viewModel = viewModel(), navController = rememberNavController())
}