package ru.student.detected.shoplist.domain

data class ShopItem(
    val name: String,
    val id: Int,
    val count: Int,
    val enabled: Boolean,
)
