package com.kiselev.financialcompanion.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("registration.php")
    suspend fun registration(@Body user: Map<String, User>): String

    @POST("login.php")
    suspend fun login(@Body user: Map<String, User>): String
}
