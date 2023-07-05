package ru.student.detected.shoplist.domain

class GetShopItem(private val shopListRepository: ShopListRepository) {
    fun getShopItem(shopItemId : Int) : ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}