package com.kiselev.financialcompanion.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.primaryColor

@Composable
fun WelcomeScreen(navController: NavController){
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Row(){
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Company logo",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(CircleShape)
                    .size(width = 40.dp, height = 40.dp))
            Text(
                text = "Финансовый ассистент",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Добро пожаловать!",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp, top = 8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily)
        Text(
            text = "Планируй будущее, управляй настоящим - раскрой свой финансовый потенциал",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 84.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            fontFamily = InterFamily,
            textAlign = TextAlign.Center)
        Button(
            onClick = { navController.navigate("Registration") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Зарегистрироваться",
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily)
        }
        OutlinedButton(
            onClick = { navController.navigate("Login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 62.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            shape = RoundedCornerShape(4.dp), border = BorderStroke(2.dp, primaryColor)
        ) {
            Text(
                text = "Войти",
                color = primaryColor,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}