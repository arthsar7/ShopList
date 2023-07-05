package ru.student.detected.shoplist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.DeleteShopItem
import ru.student.detected.shoplist.domain.EditShopItem
import ru.student.detected.shoplist.domain.GetShopList
import ru.student.detected.shoplist.domain.ShopItem

class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl

    private val getShopList = GetShopList(repository)
    private val deleteShopItem = DeleteShopItem(repository)
    private val editShopItem = EditShopItem(repository)
    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        val list = getShopList.getShopList()
        shopList.value = list
    }
    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItem.deleteShopItem(shopItem)
        getShopList()
    }
    fun editShopItem(shopItem: ShopItem) {
        editShopItem.editShopItem(shopItem)
        getShopList()
    }
    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItem.editShopItem(newItem)
    }
}