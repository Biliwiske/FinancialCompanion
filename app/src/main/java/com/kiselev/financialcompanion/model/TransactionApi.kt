package com.kiselev.financialcompanion.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionApi {
    @GET("transactions.php")
    suspend fun getTransactions(@Query("userId") userId: Int): Response<ApiResponse>

    @POST("transaction_add.php")
    suspend fun addTransaction(@Body user: Map<String, Transaction>): String
}

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val transactions: List<Transaction>?
)
