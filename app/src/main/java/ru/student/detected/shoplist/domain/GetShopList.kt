package ru.student.detected.shoplist.domain

import androidx.lifecycle.LiveData

class GetShopList(private val shopListRepository: ShopListRepository) {
    fun getShopList() : LiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}