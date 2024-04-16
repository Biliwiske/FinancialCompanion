package com.kiselev.financialcompanion.model

import androidx.annotation.DrawableRes
import com.kiselev.financialcompanion.R

data class CategoryIcon(val categoryName: String, @DrawableRes val iconResId: Int)

fun getCategoryIcon(categoryName: String): Int {
    val categoryIcons = mapOf(
        "Транспорт" to R.drawable.ic_taxi_bus,
        "Продукты" to R.drawable.ic_profile,
    )
    return categoryIcons[categoryName] ?: R.drawable.ic_profile
}
