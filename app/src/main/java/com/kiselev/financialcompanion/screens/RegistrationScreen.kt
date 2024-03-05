package com.kiselev.financialcompanion.screens

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
import androidx.compose.material.icons.filled.AccountCircle
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
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.R
import com.kiselev.financialcompanion.model.User
import com.kiselev.financialcompanion.model.UserApi
import com.kiselev.financialcompanion.ui.theme.InterFamily
import com.kiselev.financialcompanion.ui.theme.grayColor
import com.kiselev.financialcompanion.ui.theme.primaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable

fun RegistrationScreen(navController: NavController) {
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (name, setName) = remember { mutableStateOf("") }
    val (password2, setPassword2) = remember { mutableStateOf("") }

    val (errorMessage, setErrorMessage) = remember { mutableStateOf("") }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    var (error_name, setErrorName) = remember { mutableStateOf(false) }
    val (error_email, setErrorEmail) = remember { mutableStateOf(false) }
    val (error_password, setErrorPassword) = remember { mutableStateOf(false) }
    val (error_password2, setErrorPassword2) = remember { mutableStateOf(false) }

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
            fontFamily = InterFamily
        )

        Text(
            text = "Планируйте расходы, ставьте цели, достигайте успеха.",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            fontFamily = InterFamily,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Company logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .size(width = 100.dp, height = 100.dp)
        )

        Text(
            text = "Регистрация",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily
        )

        Text(
            text = "Зарегистрируйтесь, чтобы начать.",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            fontFamily = InterFamily
        )

        OutlinedTextField(
            value = name,
            onValueChange = setName,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(
                text = "Имя",
                fontWeight = FontWeight.Light,
                fontFamily = InterFamily
            ) },
            leadingIcon = { Icon(Icons.Filled.AccountCircle, contentDescription = null)},
            isError = error_name,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor)
        )

        OutlinedTextField(
            value = email,
            onValueChange = setEmail,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(
                text = "Email",
                fontWeight = FontWeight.Light,
                fontFamily = InterFamily) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            isError = error_email,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor),

        )

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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor)
        )

        OutlinedTextField(
            value = password2,
            onValueChange = setPassword2,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(
                text = "Подтверждение пароля",
                fontWeight = FontWeight.Light,
                fontFamily = InterFamily) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null)},
            isError = error_password2,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusRequesterManager.clearFocus()
                }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                errorLeadingIconColor = Color.Red,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = grayColor,
                focusedLabelColor = primaryColor)
        )

        Button(
            onClick = {
                focusRequesterManager.clearFocus()
                setIsLoading(true)

                var allFieldsValid = true

                if (name.isEmpty()) {
                    setErrorName(true)
                    allFieldsValid = false
                } else {
                    setErrorName(false)
                }

                if (email.isEmpty()) {
                    setErrorEmail(true)
                    allFieldsValid = false
                } else {
                    setErrorEmail(false)
                }

                if (password.isEmpty()) {
                    setErrorPassword(true)
                    allFieldsValid = false
                } else if (password.length < 8) {
                    setErrorMessage("Пароль должен быть не менее 8 символов")
                    setIsLoading(false)
                    return@Button
                } else {
                    setErrorPassword(false)
                }

                if (password2.isEmpty()) {
                    setErrorPassword2(true)
                    allFieldsValid = false
                } else {
                    setErrorPassword2(false)
                }

                if (!allFieldsValid) {
                    setErrorMessage("Вы ввели не все данные")
                    setIsLoading(false)
                    return@Button
                }

                if(!isEmailValid(email)) {
                    setErrorEmail(true)
                    setErrorMessage("Неверный формат email")
                    setIsLoading(false)
                    return@Button
                }

                if (password != password2) {
                    setErrorPassword(true)
                    setErrorPassword2(true)
                    setErrorMessage("Пароли не совпадают")
                    setIsLoading(false)
                    return@Button
                } else {
                    setErrorPassword(false)
                    setErrorPassword2(false)
                }

                val gson = GsonBuilder().setLenient().create()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.28/financial-companion-server/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
                val userApi = retrofit.create(UserApi::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = userApi.registration(
                            mapOf("user" to User(name = name, email = email, password = password,)
                        ))
                        if (response == "Регистрация прошла успешно") {
                            withContext(Dispatchers.Main) {
                                setIsLoading(false)
                                navController.navigate(route = "MainNavGraph")
                            }
                        } else {
                            setIsLoading(false)
                            print(response)
                            setErrorMessage("$response")
                        }
                    } catch (e: Exception) {
                        setIsLoading(false)
                        setErrorMessage("Ошибка подключения: попробуйте позже")
                        e.printStackTrace()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(text = "Зарегистрироваться",fontWeight = FontWeight.Medium,
                fontFamily = InterFamily)
        }

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
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(
            text = "Политика конфиденциальности",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {},
            color = primaryColor,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Войти",
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { navController.navigate(route = "Login") },
            color = primaryColor,
            fontWeight = FontWeight.Medium,
            fontFamily = InterFamily,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun isEmailValid(email: String): Boolean {
    val regex = Regex("""^([a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+)@([a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)+)$""")
    return regex.matches(email)
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistration(){
    RegistrationScreen(navController = rememberNavController())
}
