package com.kiselev.financialcompanion.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.kiselev.financialcompanion.controller.BudgetController
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetAdd(viewModel: BudgetController, navController: NavController)  {
    val (amount, setAmount) = remember { mutableStateOf("") }
    val (description, setDescription) = remember { mutableStateOf("") }
    val (timeStart, setTimeStart) = remember { mutableStateOf(LocalTime.now()) }
    val (dateStart, setDateStart) = remember { mutableStateOf(LocalDate.now()) }
    val (timeEnd, setTimeEnd) = remember { mutableStateOf(LocalTime.now()) }
    val (dateEnd, setDateEnd) = remember { mutableStateOf(LocalDate.now()) }
    val (type, setType) = remember { mutableStateOf("Категория") }

    val dateDialogStateStart = rememberMaterialDialogState()
    val timeDialogStateStart = rememberMaterialDialogState()

    val dateDialogStateEnd = rememberMaterialDialogState()
    val timeDialogStateEnd = rememberMaterialDialogState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(grayColor2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Назад",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text(
                text = if(type == "Счет") "На счет" else "На категорию",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "Сохранить",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable {
                        keyboardController?.hide()
                        focusRequesterManager.clearFocus()
                        println("Время старта = $timeStart, ${timeStart.toString()}")
                        viewModel.addBudget(name = description, amount = amount.toInt(), type = if (type == "Cчет") 0 else 1, start_date = dateStart.toString() + timeStart.toString(), end_date = dateEnd.toString() + timeEnd.toString(), context = context)
                        navController.popBackStack()
                    }
                    .padding(end = 16.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
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
                    containerColor = if(type == "Категория") primaryColor else Color.White,
                    contentColor = if(type == "Категория") Color.White else Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    setType("Категория")
                }
            ){
                Text(text = "На категорию",
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
                    containerColor = if(type == "Счет") redColor else Color.White,
                    contentColor = if(type == "Счет") Color.White else Color.Black,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    setType("Счет")
                }
            ){
                Text(
                    text = "На счет",
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
                Icon(imageVector = Icons.Filled.DateRange, tint = grayColor4, contentDescription = null,)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { dateDialogStateStart.show() },
                    text = "$dateStart",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily,
                    color = grayColor4
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { timeDialogStateStart.show() },
                    text = timeStart.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily,
                    color = grayColor4
                )
            }
            HorizontalDivider(
                color = grayColor,
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, tint = grayColor4, contentDescription = null,)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { dateDialogStateEnd.show() },
                    text = "$dateEnd",
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily,
                    color = grayColor4
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { timeDialogStateEnd.show() },
                    text = timeEnd.format(DateTimeFormatter.ofPattern("HH:mm")),
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
            TextField(
                value = amount,
                onValueChange = setAmount,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(
                    text = type,
                    fontWeight = FontWeight.Light,
                    fontFamily = InterFamily) },
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null)},
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
        }
    }
    MaterialDialog(
        dialogState = dateDialogStateStart,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        datepicker(
            title = "Выберите дату начала бюджета",
            colors =  DatePickerDefaults.colors(
                headerBackgroundColor = primaryColor,
                dateActiveBackgroundColor = primaryColor
            ),
            onDateChange = setDateStart
        )
    }
    MaterialDialog(
        dialogState = timeDialogStateStart,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        timepicker(
            title = "Выберите время начала бюджета",
            is24HourClock = true,
            colors =  TimePickerDefaults.colors(
                activeBackgroundColor = primaryColor,
                inactiveBackgroundColor = grayColor2,
                selectorColor = primaryColor
            ),
            onTimeChange = setTimeStart
        )
    }
    MaterialDialog(
        dialogState = dateDialogStateEnd,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        datepicker(
            title = "Выберите дату конца бюджета",
            colors =  DatePickerDefaults.colors(
                headerBackgroundColor = primaryColor,
                dateActiveBackgroundColor = primaryColor
            ),
            onDateChange = setDateEnd
        )
    }
    MaterialDialog(
        dialogState = timeDialogStateEnd,
        buttons = {
            positiveButton(text = "ОК")
            negativeButton(text = "ОТМЕНА")
        }
    ){
        timepicker(
            title = "Выберите время конца бюджета",
            is24HourClock = true,
            colors =  TimePickerDefaults.colors(
                activeBackgroundColor = primaryColor,
                inactiveBackgroundColor = grayColor2,
                selectorColor = primaryColor
            ),
            onTimeChange = setTimeEnd
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BudgetAddPreview(){
    BudgetAdd(viewModel = viewModel(), navController = rememberNavController())
}