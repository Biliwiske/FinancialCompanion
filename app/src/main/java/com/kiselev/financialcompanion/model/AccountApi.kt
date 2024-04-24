package com.kiselev.financialcompanion.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AccountApi {
    @GET("accounts.php")
    suspend fun getAccounts(@Query("userId") userId: Int): Response<ApiResponseAccount>

    @POST("account_add.php")
    suspend fun addAccount(@Body user: Map<String, Account>): String
}

data class ApiResponseAccount(
    val success: Boolean,
    val message: String,
    val accounts: List<Account>?
)
