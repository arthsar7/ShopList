package ru.student.detected.shoplist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.DeleteShopItemUseCase
import ru.student.detected.shoplist.domain.EditShopItemUseCase
import ru.student.detected.shoplist.domain.GetShopListUseCase
import ru.student.detected.shoplist.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)

    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    val shopList = GetShopListUseCase(repository).getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }
    fun editShopItem(shopItem: ShopItem) {
        editShopItemUseCase.editShopItem(shopItem)
    }
    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}