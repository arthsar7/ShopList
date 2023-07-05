package ru.student.detected.shoplist.data

import ru.student.detected.shoplist.domain.ShopItem
import ru.student.detected.shoplist.domain.ShopItem.Companion.UNDEFINED_ID
import ru.student.detected.shoplist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository{
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0
    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == UNDEFINED_ID) shopItem.id = autoIncrementId++
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopList[shopList.indexOf(shopList.find { it.id == shopItem.id })] = shopItem
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId } ?: throw RuntimeException("Item with id $shopItemId not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }
}