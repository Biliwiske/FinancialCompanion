package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.Budget
import com.kiselev.financialcompanion.model.BudgetApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class BudgetController : ViewModel() {
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val budgetApi = retrofit.create(BudgetApi::class.java)

    suspend fun getBudgets(context: Context): List<Budget>? {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = budgetApi.getBudgets(userId)
                if (response.isSuccessful) {
                    response.body()?.budgets
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

    suspend fun calculateBudget(category: String, context: Context): Pair<Int, Int> {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = budgetApi.calculate_budget(mapOf("id_user" to userId.toString(), "category" to category))
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                if (success) {
                    val totalAmount = jsonObject.getInt("total_amount")
                    val rowCount = jsonObject.getInt("row_count")
                    Pair(totalAmount, rowCount)
                } else {
                    handleResponseError(jsonObject.getString("message"))
                    Pair(0, 0)
                }
            } else {
                handleUserIdIsNullError()
                Pair(0, 0)
            }
        } catch (e: Exception) {
            handleError(e)
            Pair(0, 0)
        }
    }

    fun addBudget(name: String, amount: Int, type: Int, start_date: String, end_date: String, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = readUserId(context)
                println("Конец бюджета = $end_date")
                val response = budgetApi.addBudget(mapOf("budget" to Budget(id = -1, name = name, amount = amount, type = type, start_date = start_date, end_date = end_date, id_user = userId!!)))
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