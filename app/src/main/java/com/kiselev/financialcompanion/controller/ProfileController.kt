package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.AccountApi
import com.kiselev.financialcompanion.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ProfileController: ViewModel() {
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val accountApi = retrofit.create(AccountApi::class.java)

    suspend fun getAccounts(context: Context) : List<Account>? {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = accountApi.getAccounts(userId)
                if (response.isSuccessful) {
                    response.body()?.accounts
                } else {
                    handleResponseError(response.message())
                    null
                }
            } else {
                handleUserIdIsNullError()
                null
            }
        } catch (e: Exception) {
            handleError(e)
            null
        }
    }

    fun addAccount(name: String, balance: Int, currency: String, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = readUserId(context)
                val response = accountApi.addAccount(mapOf("account" to Account(id_account = -1, name = name, balance = balance, currency = currency, id_user = userId!!)))
                println("response = $response")
                withContext(Dispatchers.Main) {
                    //handleLoginResponse(response, navController, context)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    //handleLoginError(e)
                }
            } finally {
                //isLoading = false
            }
        }
    }

    private fun handleResponseError(message: String?) {
        println("Server error: $message")
    }

    private fun handleUserIdIsNullError() {
        println("Отсутствие id пользователя")
    }

    private fun handleError(e: Exception) {
        e.printStackTrace()
    }

    private suspend fun readUserId(context: Context): Int? {
        val dataStore = StorageController(context)
        return withContext(Dispatchers.IO) {
            dataStore.getId().firstOrNull()
        }
    }
}