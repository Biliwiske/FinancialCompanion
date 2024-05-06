package com.kiselev.financialcompanion.model

data class Transaction(
    val id_transaction: Int,
    val date: String,
    val amount: Int,
    val description: String,
    val type: Int,
    val category: String,
    val user_id: Int,
    val id_account: String,
    val account_name: String? = null
)