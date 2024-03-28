package com.kiselev.financialcompanion.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.User
import com.kiselev.financialcompanion.model.UserApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoginViewModel : ViewModel() {

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.59/financial-companion-server/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val userApi = retrofit.create(UserApi::class.java)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = ""

            try {

                val response: String = userApi.login(mapOf("user" to User(name="", email = email, password = password)))
                handleLoginResponse(response)
            } catch (e: Exception) {
                handleLoginError(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleLoginResponse(response: String) {
        val jsonResponse = JSONObject(response)
        val success = jsonResponse.getBoolean("success")
        val message = jsonResponse.getString("message")

        if (success) {
            val id = jsonResponse.getInt("id")
            saveUserId(id)
            // Вам может потребоваться добавить логику для навигации сюда
        } else {
            _errorMessage.value = message
        }
    }

    private fun handleLoginError(e: Exception) {
        _errorMessage.value = "Ошибка подключения: попробуйте позже"
        e.printStackTrace()
    }

    private fun saveUserId(userId: Int) {
        //val sharedPreferences = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        //val editor = sharedPreferences.edit()
        //editor.putInt("user_id", id)
        //editor.apply()
    }
}
