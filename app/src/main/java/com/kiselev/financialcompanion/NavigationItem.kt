package com.kiselev.financialcompanion

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Operation : NavigationItem("operation", R.drawable.ic_profile, "Операции")
    object Budget : NavigationItem("budget", R.drawable.ic_profile, "Бюджет")
    object Graph : NavigationItem("graph", R.drawable.ic_profile, "Графики")
    object Category : NavigationItem("category", R.drawable.ic_grapfic, "Категории")
    object Profile : NavigationItem("profile", R.drawable.ic_profile, "Профиль")
}
