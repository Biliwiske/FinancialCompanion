package com.kiselev.financialcompanion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.ui.theme.InterFamily


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun OperationAdd() {
    val texts = listOf(
        "Название",
        "Дата",
        "Сумма",
        "Категория",
        "Кошелек",
        "Описание",
    )
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
                text = "Доход",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = "Сохранить",
                fontFamily = InterFamily,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
            )
        }
        for (i in texts.indices) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = texts[i],
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .align(Alignment.Start)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = "",
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {},
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start),
                    placeholder = {
                        Text(
                            text = "Введите",
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        )
                    }
                )
            }
            Divider(modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}
