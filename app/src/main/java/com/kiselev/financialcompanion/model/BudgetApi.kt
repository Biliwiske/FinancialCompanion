package com.kiselev.financialcompanion.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BudgetApi {
    @GET("budgets.php")
    suspend fun getBudgets(@Query("userId") userId: Int): Response<ApiResponseBudget>

    @POST("budget_add.php")
    suspend fun addBudget(@Body user: Map<String, Budget>): String

    @POST("budget_calculate.php")
    suspend fun calculate_budget(@Body data: Map<String, String>): String
}


data class ApiResponseBudget(
    val success: Boolean,
    val message: String,
    val budgets: List<Budget>?
)
