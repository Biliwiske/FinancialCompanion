package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.controller.StorageController.Companion.AUTHENTICATED_STATUS
import com.kiselev.financialcompanion.model.User
import com.kiselev.financialcompanion.model.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoginViewModel : ViewModel() {
    var errorEmail by mutableStateOf (false)
    var errorPassword by mutableStateOf (false)

    var errorMessage by mutableStateOf ("")
    var isLoading by mutableStateOf (false)

    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val userApi = retrofit.create(UserApi::class.java)

    fun login(email: String, password: String, navController: NavController, context: Context) {
        errorMessage = ""
        isLoading = true
        if(!validateFields(email,password)) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userApi.login(mapOf("user" to User(name="", email = email, password = password)))
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

    private fun validateFields(email: String, password: String): Boolean{
        errorEmail = email.isEmpty()
        errorPassword = password.isEmpty()

        if (errorEmail || errorPassword) {
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

        return true
    }

    private fun isEmailValid(email: String): Boolean {
        val regex = Regex("""^([a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+)@([a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)+)$""")
        return regex.matches(email )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleLoginResponse(response: String, navController: NavController, context: Context) {
        val jsonResponse = JSONObject(response)
        val success = jsonResponse.getBoolean("success")
        val message = jsonResponse.getString("message")

        if (success) {
            val id = jsonResponse.getInt("id")
            saveUserId(id, context)

            val dataStore = StorageController(context)
            GlobalScope.launch {
                dataStore.saveAuthenticationStatus(AUTHENTICATED_STATUS)
            }

            navController.navigate(route = "MainScreen")
        } else if(message == "Неверные данные"){
            isLoading = false
            errorEmail = true
            errorPassword = true
            errorMessage = message
        }
    }


    private fun handleLoginError(e: Exception) {
        errorMessage = "Ошибка подключения: попробуйте позже"
        e.printStackTrace()
    }

    private fun saveUserId(userId: Int, context: Context) {
        val dataStore = StorageController(context)
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.saveId(userId)
        }
    }
}
