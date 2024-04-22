package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.User
import com.kiselev.financialcompanion.model.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RegistrationController : ViewModel() {
    var errorName by mutableStateOf (false)
    var errorEmail by mutableStateOf (false)
    var errorPassword by mutableStateOf (false)
    var errorPassword2 by mutableStateOf (false)

    var errorMessage by mutableStateOf ("")
    var isLoading by mutableStateOf (false)

    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val userApi = retrofit.create(UserApi::class.java)

    fun registerUser(name: String, email: String, password: String, password2:String,
                     navController: NavController, context: Context) {
        errorMessage = ""
        isLoading = true
        if(!validateFields(name,email,password, password2)) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userApi.registration(mapOf("user" to User(name = name, email = email, password = password)))
                withContext(Dispatchers.Main) {
                    handleLoginResponse(response, navController, context)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    handleLoginError(e)
                }
            } finally {
                isLoading = false
            }
        }
    }

    private fun validateFields(name: String, email: String, password: String, password2:String): Boolean{
        errorName = name.isEmpty()
        errorEmail = email.isEmpty()
        errorPassword = password.isEmpty()
        errorPassword2 = password2.isEmpty()

        if (errorName || errorEmail || errorPassword || errorPassword2) {
            errorMessage = "Пожалуйста, заполните все поля."
            isLoading = false
            return false
        }

        if (!isEmailValid(email)){
            errorMessage = "Пожалуйста, введите email корректно."
            errorEmail = true
            isLoading = false
            return false
        }

        if (password.length < 8){
            errorMessage = "Пароль должен быть не менее 8 символов."
            errorPassword = true
            isLoading = false
            return false
        }

        if (password.length > 64){
            errorMessage = "Пароль должен быть не более 64 символов."
            errorPassword = true
            isLoading = false
            return false
        }

        if (password != password2) {
            errorMessage = "Пароли не совпадают."
            errorPassword = true
            errorPassword2 = true
            isLoading = false
            return false
        }

        return true
    }

    private fun handleLoginResponse(response: String, navController: NavController, context: Context) {
        val jsonResponse = JSONObject(response)
        val success = jsonResponse.getBoolean("success")
        val message = jsonResponse.getString("message")

        if (success) {
            val id = jsonResponse.getInt("id")
            saveUserId(id, context)
            navController.navigate(route = "MainNavGraph")
        } else if(message == "Данный email уже зарегистрирован"){
            errorEmail = true
            errorMessage = message
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("""^([a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+)@([a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)+)$""")
        return regex.matches(email )
    }

    private fun handleLoginError(e: Exception) {
        errorMessage = "Ошибка подключения: попробуйте позже"
        e.printStackTrace()
    }

    private fun saveUserId(userId: Int, context: Context) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.apply()
    }
}
