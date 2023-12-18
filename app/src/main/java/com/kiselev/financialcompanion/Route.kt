package com.kiselev.financialcompanion

const val ROOT_ROUTE = "root"
const val BASE_ROUTE = "base"
const val PRODUCT_ROUTE = "product"

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Operation : NavigationItem("operation", R.drawable.ic_profile, "Операции")
    object Budget : NavigationItem("budget", R.drawable.ic_profile, "Бюджет")
    object Graph : NavigationItem("graph", R.drawable.ic_profile, "Графики")
}

sealed class OtherItem(val route: String) {
    object TransactionAdd : OtherItem("TransactionAdd")
}
