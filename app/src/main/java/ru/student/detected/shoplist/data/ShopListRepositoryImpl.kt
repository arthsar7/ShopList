package ru.student.detected.shoplist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.student.detected.shoplist.domain.ShopItem
import ru.student.detected.shoplist.domain.ShopItem.Companion.UNDEFINED_ID
import ru.student.detected.shoplist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0
    private val shopListLD = MutableLiveData<List<ShopItem>>()
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == UNDEFINED_ID) shopItem.id = autoIncrementId++
        shopList.add(shopItem)
        updateList()
    }

    init {
        for (i in 0 until 100) {
            addShopItem(ShopItem("Name $i", i, Random.nextBoolean()))
        }
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopList[shopList.indexOf(shopList.find { it.id == shopItem.id })] = shopItem
        updateList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Item with id $shopItemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }
}