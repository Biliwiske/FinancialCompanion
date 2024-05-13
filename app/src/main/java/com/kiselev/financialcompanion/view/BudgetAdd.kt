package com.kiselev.financialcompanion.view

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
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.kiselev.financialcompanion.controller.BudgetController
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.grayColor2
import com.kiselev.financialcompanion.ui.theme.grayColor4
import com.kiselev.financialcompanion.ui.theme.primaryColor
import com.kiselev.financialcompanion.ui.theme.redColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun BudgetAdd(viewModel: BudgetController, navController: NavController, category: String?)  {
    val (amount, setAmount) = rememberSaveable { mutableStateOf("") }
    val (dateStart, setDateStart) = remember { mutableStateOf(LocalDate.now()) }
    val (dateEnd, setDateEnd) = remember { mutableStateOf(LocalDate.now()) }
    val (type, setType) = remember { mutableStateOf("Категория") }
    val (categoryName, _) = remember { mutableStateOf(category) }

    val dateDialogStateStart = rememberMaterialDialogState()
    val dateDialogStateEnd = rememberMaterialDialogState()

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
                        if (categoryName != null) {
                            viewModel.addBudget(
                                name = categoryName,
                                amount = amount.toInt(),
                                type = if (type == "Cчет") 0 else 1,
                                start_date = dateStart.toString(),
                                end_date = dateEnd.toString(),
                                context = context
                            )
                        }
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
                Icon(imageVector = Icons.Filled.DateRange, tint = grayColor4, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .clickable { navController.navigate(route = "SelectionList") }
                ) {
                    Text(
                        text = "Дата начала",
                        color = grayColor4,
                        fontSize = 12.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily
                    )
                    Text(
                        modifier = Modifier
                            .clickable { dateDialogStateStart.show() },
                        text = "$dateStart",
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily,
                        color = grayColor4
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
                    .padding(start = 12.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, tint = grayColor4, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                        .clickable { navController.navigate(route = "SelectionList") }
                ) {
                    Text(
                        text = "Дата конца",
                        color = grayColor4,
                        fontSize = 12.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily
                    )
                    Text(
                        modifier = Modifier
                            .clickable { dateDialogStateEnd.show() },
                        text = "$dateEnd",
                        fontWeight = FontWeight.Light,
                        fontFamily = InterFamily,
                        color = grayColor4
                    )
                }
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
}

@Preview(showBackground = true)
@Composable
fun BudgetAddPreview(){
    BudgetAdd(viewModel = viewModel(), navController = rememberNavController(), category = "Транспорт")
}