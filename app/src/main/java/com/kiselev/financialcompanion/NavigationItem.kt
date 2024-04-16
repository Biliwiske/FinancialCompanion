package com.kiselev.financialcompanion

sealed class NavigationItem(var number: Int,var route: String, var icon: Int, var title: String) {
    object Operation : NavigationItem(0,"operation", R.drawable.ic_transfer2, "Операции")
    object Budget : NavigationItem(1,"budget", R.drawable.ic_chart, "Бюджет")
    object Graph : NavigationItem(2,"graph", R.drawable.ic_graphs, "Графики")
    object Profile : NavigationItem(4,"profile", R.drawable.ic_icon, "Профиль")
    object Category : NavigationItem(3,"category", R.drawable.ic_profile, "Категории")
}
