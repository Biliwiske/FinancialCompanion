package com.kiselev.financialcompanion.model

data class Transaction(
    val idTransaction: Int,
    val date: String,
    val amount: Int,
    val description: String,
    val type: Int,
    val category: String,
    val account_name: String
)