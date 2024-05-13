package com.kiselev.financialcompanion.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.controller.ProfileController
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.Budget
import com.kiselev.financialcompanion.model.CategoryIcon
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor3
import com.kiselev.financialcompanion.ui.theme.grayColor4

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AccountListScreen(viewModel: ProfileController, navController: NavController) {
    val context = LocalContext.current
    var accounts by remember { mutableStateOf<List<Account>?>(null) }
    LaunchedEffect(key1 = Unit) {
        accounts = viewModel.getAccounts(context)
    }

    println("Список полученных аккаунтов = $accounts")
    Column {
        Row(modifier = Modifier.fillMaxWidth())
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Назад",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Text(
                    text = "Выберите счет",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
        accounts?.let { AccountView(accounts = it, navController = navController) }
    }
}

@Composable
fun AccountItem(id: Int, name: String, balance: Int, checked: Boolean, navController: NavController) {
    val checkboxIcon = if (checked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("account_name", name)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("account_id", id.toString())
                navController.popBackStack()
            }
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .size(35.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(2.dp)
                )
        ) {
            Image(
                modifier = Modifier.padding(2.dp),
                painter = painterResource(id = R.drawable.vector),
                contentDescription = "Иконка категории"
            )
        }
        Column (
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically).padding(start = 8.dp)
        ){
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Text(
                text = "баланс: $balance",
                color = grayColor3,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }

        Image(
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = checkboxIcon),
            contentDescription = "Иконка категории"
        )
    }
}

@Composable
private fun AccountView(accounts: List<Account>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(items = accounts) { account ->
            AccountItem(
                id = account.id_account,
                name = account.name,
                balance = account.balance,
                checked = false,
                navController = navController
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountListPreview(){
    AccountItem(10 ,"Наличные", 12000, false, rememberNavController())
}