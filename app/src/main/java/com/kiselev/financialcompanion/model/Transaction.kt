package com.kiselev.financialcompanion.model

data class Transaction(
    val id: Int,
    val name: String,
    val amount: Int,
    val description: String,
    val type: String,
    val category: String,
)