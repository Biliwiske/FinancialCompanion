package com.kiselev.financialcompanion

import retrofit2.http.GET

interface TransactionApi {
    @GET("transactions.php")
    suspend fun getTransactions(): Transaction
}
