package com.kiselev.financialcompanion.model

import retrofit2.http.GET
import retrofit2.http.Query

interface GraphApi {
    @GET("category_calculated.php")
    suspend fun getCategoryCalculated(@Query("userId") userId: Int): String
}
