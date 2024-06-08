package com.kiselev.financialcompanion.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
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
import com.kiselev.financialcompanion.controller.OperationController
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor4
import com.kiselev.financialcompanion.ui.theme.primaryColor
import com.kiselev.financialcompanion.ui.theme.redColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("Range")
@Composable
fun OperationAdd(viewModel: OperationController, navController: NavController, category: String?, account: String?, id: String?) {

    val (time, setTime) = rememberSaveable { mutableStateOf(LocalTime.now()) }
    val (date, setDate) = rememberSaveable { mutableStateOf(LocalDate.now())}
    val (categoryName, _) = remember { mutableStateOf(category) }
    val (accountName, _) = remember { mutableStateOf(account) }
    val (amount, setAmount) = rememberSaveable { mutableStateOf("") }
    val (description, setDescription) = rememberSaveable { mutableStateOf("") }
    val regularState = rememberSaveable { mutableStateOf(false)}
    val (type, setType) = rememberSaveable { mutableStateOf("Доход")}

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Назад",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text(
                text = type,
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
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
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus()
                        viewModel.addTransaction(
                            date = "$date $time",
                            amount = amount.toInt(),
                            description = description,
                            type = if (type == "Доход") 0 else 1,
                            category = categoryName!!,
                            account_name = accountName!!,
                            account_id = id!!,
                            context = context
                        )
                        navController.popBackStack()
                    }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonColors(
                    containerColor = if(type == "Доход") primaryColor else Color.White,
                    contentColor = if(type == "Доход") Color.White else Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    setType("Доход")
                }
            ){
                Text(text = "Доход",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                    )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RectangleShape,
                colors = ButtonColors(
                    containerColor = if(type == "Расход") redColor else Color.White,
                    contentColor = if(type == "Расход") Color.White else Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    setType("Расход")
                }
            ){
                Text(
                    text = "Расход",
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
                .verticalScroll(ScrollState(0))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus()
                    })
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, tint = grayColor4, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { dateDialogState.show() },
                    text = "$date",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily,
                    color = grayColor4
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { timeDialogState.show() },
                    text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily,
                    color = grayColor4
                )
            }
            HorizontalDivider(
                color = grayColor,
                thickness = 1.dp
            )
            TextField(
                value = amount,
                onValueChange = setAmount,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = "Сумма",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null)},
                isError = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Decimal),
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
                value = description,
                onValueChange = setDescription,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = "Описание",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null)},
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
                    focusedLabelColor = primaryColor))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Edit, tint = grayColor4, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .clickable { navController.navigate(route = "SelectionList") }
                ){
                    categoryName?.let{
                        Text(
                            text = "Категория",
                            color = grayColor4,
                            fontSize = 12.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                ),
                            ),
                            fontWeight = FontWeight.Light,
                            fontFamily = InterFamily)
                    }
                    Text(
                        text = categoryName ?: "Категория",
                        color = if(categoryName != null) Color.Black else grayColor4,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily
                    )
                }
            }
            HorizontalDivider(
                color = grayColor,
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Edit, tint = grayColor4, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .clickable { navController.navigate(route = "AccountList") }
                ){
                    accountName?.let{
                        Text(
                            text = "Счет",
                            color = grayColor4,
                            fontSize = 12.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                ),
                            ),
                            fontWeight = FontWeight.Light,
                            fontFamily = InterFamily)
                    }
                    Text(
                        text = accountName ?: "Счет",
                        color = if(accountName != null) Color.Black else grayColor4,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily
                    )
                }
            }
            HorizontalDivider(
                color = grayColor,
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Повторять операцию",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .padding(start = 12.dp),
                    fontWeight = FontWeight.Normal,
                    fontFamily = InterFamily,
                    fontSize = 16.sp,
                    color = grayColor4
                )
                Switch(
                    checked = regularState.value,
                    onCheckedChange = { regularState.value = it },
                )
            }
        }
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        datepicker(
            title = "Выберите дату транзакции",
            colors =  DatePickerDefaults.colors(
                headerBackgroundColor = primaryColor,
                dateActiveBackgroundColor = primaryColor
            ),
            onDateChange = setDate
        )
    }
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        timepicker(
            title = "Выберите время транзакции",
            is24HourClock = true,
            colors =  TimePickerDefaults.colors(
                activeBackgroundColor = primaryColor,
                inactiveBackgroundColor = grayColor2,
                selectorColor = primaryColor
            ),
            onTimeChange = setTime
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Previews(){
    OperationAdd(
        viewModel = viewModel(),
        rememberNavController(),
        "Транспорт",
        "Наличные",
        "10"
    )
}