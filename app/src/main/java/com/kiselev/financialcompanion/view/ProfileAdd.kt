package com.kiselev.financialcompanion.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.controller.ProfileController
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor

@Composable
fun ProfileAdd(viewModel: ProfileController, navController: NavController) {
    val (name, setName) = remember { mutableStateOf("") }
    val (balance, setBalance) = remember { mutableStateOf("")}
    val (currency, setCurrency) = remember { mutableStateOf("")}

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusRequesterManager.clearFocus()
                })
            }
    ){
        Row{
            IconButton(
                onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Назад",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text(
                text = "Настройка счета",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "Сохранить",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        viewModel.addAccount(name, balance.toInt(), currency, context)
                        navController.popBackStack()
                    }
            )

        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
            thickness = 0.dp,
            color = grayColor
        )
        Column (
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        ){
            TextField(
                value = name,
                onValueChange = setName,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = "Название",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null)},
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red,
                    errorLeadingIconColor = Color.Red,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = grayColor,
                    focusedLabelColor = primaryColor))
            TextField(
                value = balance,
                onValueChange = setBalance,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = "Текущий баланс",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null)},
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Decimal),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red,
                    errorLeadingIconColor = Color.Red,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = grayColor,
                    focusedLabelColor = primaryColor
                )
            )
            TextField(
                value = currency,
                onValueChange = setCurrency,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = "Валюта",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.Menu, contentDescription = null)},
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red,
                    errorLeadingIconColor = Color.Red,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = grayColor,
                    focusedLabelColor = primaryColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAddPreview(){
    ProfileAdd(viewModel = viewModel(), navController = rememberNavController())
}