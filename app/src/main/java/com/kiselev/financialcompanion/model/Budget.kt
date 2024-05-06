package com.kiselev.financialcompanion.model

data class Budget(
    val id: Int,
    val name: String,
    val amount: Int,
    val type: Int,
    val start_date: String,
    val end_date: String,
    val id_user: Int
)