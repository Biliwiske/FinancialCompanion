package com.kiselev.financialcompanion

sealed class NavigationItem(var number: Int,var route: String, var icon: Int, var title: String) {
    object Operation : NavigationItem(0,"operation", R.drawable.ic_profile, "Операции")
    object Budget : NavigationItem(1,"budget", R.drawable.ic_profile, "Бюджет")
    object Graph : NavigationItem(2,"graph", R.drawable.ic_profile, "Графики")
    object Category : NavigationItem(3,"category", R.drawable.ic_grapfic, "Категории")
    object Profile : NavigationItem(4,"profile", R.drawable.ic_profile, "Профиль")
}
