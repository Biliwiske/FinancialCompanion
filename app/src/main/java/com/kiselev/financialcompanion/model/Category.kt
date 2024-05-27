package com.kiselev.financialcompanion.model

import androidx.annotation.DrawableRes
import com.kiselev.financialcompanion.R

data class CategoryIcon(val categoryName: String, @DrawableRes val iconResId: Int)

fun getCategoryIcon(categoryName: String): Int {
    val categoryIcons = mapOf(
        "Продукты" to R.drawable.ic_shopping_bag,
        "Коммунальные услуги" to R.drawable.ic_faucet,
        "Транспорт" to R.drawable.ic_taxi_bus,
        "Связь" to R.drawable.ic_phone  ,
        "Техника" to R.drawable.ic_computer,
        "Здоровье" to R.drawable.ic_health,
        "Образование" to R.drawable.ic_education,
        "Одежда" to R.drawable.ic_clothes,
        "Развлечения" to R.drawable.ic_games,
        "Спорт" to R.drawable.ic_sport,
        "Благотворительность" to R.drawable.ic_charity,

        "Прочее" to R.drawable.ic_option,

        "Зарплата" to R.drawable.ic_money,
        "Инвестиции" to R.drawable.ic_taxi_bus,
        "Пособие" to R.drawable.ic_taxi_bus,
        "Бизнес" to R.drawable.ic_taxi_bus,
        "Аренда" to R.drawable.ic_taxi_bus,
        "Транспорт" to R.drawable.ic_taxi_bus,
        "Подарок" to R.drawable.ic_taxi_bus,
        "Лотерея" to R.drawable.ic_lottery,
        "Награда" to R.drawable.ic_taxi_bus,
    )
    return categoryIcons[categoryName] ?: R.drawable.ic_logo
}

fun getCategoryIconsListIncome(): List<CategoryIcon> {
    val categoryIcons = mapOf(
        "Зарплата" to R.drawable.ic_money,
        "Инвестиции" to R.drawable.ic_taxi_bus,
        "Пособие" to R.drawable.ic_taxi_bus,
        "Бизнес" to R.drawable.ic_taxi_bus,
        "Аренда" to R.drawable.ic_taxi_bus,
        "Подарок" to R.drawable.ic_taxi_bus,
        "Лотерея" to R.drawable.ic_lottery,
        "Награда" to R.drawable.ic_taxi_bus,
        "Прочее" to R.drawable.ic_taxi_bus,
    )
    val categories = mutableListOf<CategoryIcon>()

    for ((categoryName, iconResId) in categoryIcons) {
        categories.add(CategoryIcon(categoryName, iconResId))
    }

    return categories
}

fun getCategoryIconsListExpenses(): List<CategoryIcon> {
    val categoryIcons = mapOf(
        "Продукты" to R.drawable.ic_shopping_bag,
        "Коммунальные услуги" to R.drawable.ic_faucet,
        "Транспорт" to R.drawable.ic_taxi_bus,
        "Подарок" to R.drawable.ic_games,
        "Связь" to R.drawable.ic_phone,
        "Техника" to R.drawable.ic_computer,
        "Здоровье" to R.drawable.ic_health,
        "Образование" to R.drawable.ic_education,
        "Одежда" to R.drawable.ic_clothes,
        "Развлечения" to R.drawable.ic_games,
        "Спорт" to R.drawable.ic_sport,
        "Благотворительность" to R.drawable.ic_charity,
        "Прочее" to R.drawable.ic_option,
    )
    val categories = mutableListOf<CategoryIcon>()

    for ((categoryName, iconResId) in categoryIcons) {
        categories.add(CategoryIcon(categoryName, iconResId))
    }

    return categories
}
