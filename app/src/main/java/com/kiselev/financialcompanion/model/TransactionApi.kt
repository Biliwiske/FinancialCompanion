package com.kiselev.financialcompanion.model

import retrofit2.http.GET

interface TransactionApi {
    @GET("transactions.php")
    suspend fun getTransactions(): List<Transaction>
}
