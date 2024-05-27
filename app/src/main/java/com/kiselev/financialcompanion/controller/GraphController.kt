package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.Account
import com.kiselev.financialcompanion.model.AccountApi
import com.kiselev.financialcompanion.model.Budget
import com.kiselev.financialcompanion.model.BudgetApi
import com.kiselev.financialcompanion.model.GraphApi
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.model.TransactionApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class GraphController : ViewModel() {
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val graphApi = retrofit.create(GraphApi::class.java)
    private val accountApi = retrofit.create(AccountApi::class.java)
    private val transactionApi = retrofit.create(TransactionApi::class.java)

    suspend fun getCategoryCalculated(context: Context): Map<String, Pair<Int, Int>> {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = graphApi.getCategoryCalculated(userId)
                val jsonObject = JSONObject(response)
                val success = jsonObject.getBoolean("success")
                if (success) {
                    val categoriesArray = jsonObject.getJSONArray("categories")
                    val categoriesMap = mutableMapOf<String, Pair<Int, Int>>()
                    for (i in 0 until categoriesArray.length()) {
                        val categoryObject = categoriesArray.getJSONObject(i)
                        val categoryName = categoryObject.getString("category")
                        val totalAmount = categoryObject.getInt("total_amount")
                        val rowCount = categoryObject.getInt("row_count")
                        categoriesMap[categoryName] = Pair(totalAmount, rowCount)
                    }
                    categoriesMap
                } else {
                    handleResponseError(jsonObject.getString("message"))
                    emptyMap()
                }
            } else {
                handleUserIdIsNullError()
                emptyMap()
            }
        } catch (e: Exception) {
            handleError(e)
            emptyMap()
        }
    }

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

    suspend fun getTransactions(context: Context) : List<Transaction>? {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = transactionApi.getTransactions(userId)
                if (response.isSuccessful) {
                    response.body()?.transactions
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