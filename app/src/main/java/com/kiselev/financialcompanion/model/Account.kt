package com.kiselev.financialcompanion.model

data class Account(
    val id_account: Int,
    val name: String,
    val balance: Int,
    val currency: String,
    val id_user: Int
)