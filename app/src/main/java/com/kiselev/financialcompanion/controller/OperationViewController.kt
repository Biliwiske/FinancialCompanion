package com.kiselev.financialcompanion.controller

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.kiselev.financialcompanion.model.ApiResponse
import com.kiselev.financialcompanion.model.Transaction
import com.kiselev.financialcompanion.model.TransactionApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.Date

class OperationViewController : ViewModel() {
    private var transactions : List<Transaction>? = null
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.30/financial-companion-server/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val transactionApi = retrofit.create(TransactionApi::class.java)

    suspend fun getTransactions(context: Context) : List<Transaction>? {
        return try {
            val userId = readUserId(context)
            if (userId != null) {
                val response = transactionApi.getTransactions(userId)
                println("Результат запроса = ${handleResponse(response)}")
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

    private fun handleResponse(response: Response<ApiResponse>): List<Transaction>? {
        val apiResponse = response.body()
        val transactionList = apiResponse?.transactions
        println("Список транзакций 1 $transactionList")
        if (transactionList != null) {
            transactions = transactionList.map { transactionDto ->
                Transaction(
                    idTransaction = transactionDto.idTransaction,
                    date = transactionDto.date,
                    amount = transactionDto.amount,
                    description = transactionDto.description,
                    type = transactionDto.type,
                    category = transactionDto.category,
                    account_name = transactionDto.account_name
                )
            }
        }
        println("транзакции $transactions")
        println("Список транзакций 2 $transactionList")
        return transactionList
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

    @SuppressLint("SimpleDateFormat")
    fun calculateTransactions(transactions: List<Transaction>): Map<Date?, Int> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dailySum = mutableMapOf<Date?, Int>()

        for (transaction in transactions) {
            val transactionDate = dateFormat.parse(transaction.date)

            val amountToAdd = if (transaction.type == 1) -transaction.amount else transaction.amount

            if (dailySum.containsKey(transactionDate)) {
                dailySum[transactionDate] = dailySum[transactionDate]!! + amountToAdd
            } else {
                dailySum[transactionDate] = amountToAdd
            }
        }

        return dailySum
    }
}
