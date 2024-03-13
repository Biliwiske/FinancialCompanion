package com.kiselev.financialcompanion.model

import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("registration.php")
    suspend fun registration(@Body user: Map<String, User>): String

    @POST("login.php")
    fun login(@Body user: Map<String, User>): String
}
