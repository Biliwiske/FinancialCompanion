package com.kiselev.financialcompanion.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.model.User
import com.kiselev.financialcompanion.model.UserApi
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavController){
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (errorMessage, setErrorMessage) = remember { mutableStateOf("") }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (error_email, setErrorEmail) = remember { mutableStateOf(false) }
    val (error_password, setErrorPassword) = remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current

    if(isLoading){
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            color = primaryColor
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(all = 16.dp)
            .verticalScroll(ScrollState(0))
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    focusRequesterManager.clearFocus()
                })
            }
    ) {
        Text(
            text = "Финансовый ассистент",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily)

        Text(
            text = "Планируйте расходы, ставьте цели, достигайте успеха.",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            fontFamily = InterFamily,
            textAlign = TextAlign.Center)

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Company logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .size(width = 100.dp, height = 100.dp))

        Text(
            text = "Вход",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily)

        Text(
            text = "Войдите, если у вас уже есть аккаунт.",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            fontFamily = InterFamily)

        OutlinedTextField(
            value = email,
            onValueChange = setEmail,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(
                text = "Электронная почта",
                fontWeight = FontWeight.Light,
                fontFamily = InterFamily) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null)},
            isError = error_email,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor))

        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(
                text = "Пароль",
                fontWeight = FontWeight.Light,
                fontFamily = InterFamily) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null)},
            isError = error_password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusRequesterManager.clearFocus() }),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor))

        Button(
            onClick = {
                focusRequesterManager.clearFocus()
                setIsLoading(true)
                setErrorMessage("")

                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.28/financial-companion-server/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
                val userApi = retrofit.create(UserApi::class.java)
                    try {
                        val response : String = userApi.login(mapOf("user" to User(email = email, password = password)))

                        val jsonResponse = JSONObject(response)
                        val success = jsonResponse.getBoolean("success")
                        val message = jsonResponse.getString("message")

                        val sharedPreferences = this.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                        val userId = jsonResponse.getString("userId")
                        val editor = sharedPreferences.edit()
                        editor.putString("userId", userId)
                        editor.apply()


                        if (success) {
                                setIsLoading(false)
                                navController.navigate(route = "MainNavGraph")

                        } else {
                            setErrorEmail(true)
                            setErrorPassword(true)
                            setIsLoading(false)
                            setErrorMessage(message)
                        }
                    } catch (e: Exception) {
                        setIsLoading(false)
                        setErrorMessage("Ошибка подключения: попробуйте позже")
                        e.printStackTrace()
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Войти",
                fontWeight = FontWeight.Medium,
                fontFamily = InterFamily) }

        if (errorMessage.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Ошибка",
                    tint = Color.Red)
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color.Red,
                    fontWeight = FontWeight.Medium,
                    fontFamily = InterFamily,
                    style = MaterialTheme.typography.bodyMedium) }
        }

        Text(
            text = "Забыли пароль?",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {navController.navigate(route = "MainNavGraph")},
            color = primaryColor,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium)

        Text(
            text = "Создать аккаунт",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate(route = "Registration") },
            color = primaryColor,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



@Preview(showBackground = true)
@Composable
fun Preview(){
    LoginScreen(navController = rememberNavController())
}
