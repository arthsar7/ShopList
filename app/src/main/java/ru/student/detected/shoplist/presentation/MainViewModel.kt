package ru.student.detected.shoplist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.student.detected.shoplist.domain.DeleteShopItemUseCase
import ru.student.detected.shoplist.domain.EditShopItemUseCase
import ru.student.detected.shoplist.domain.GetShopListUseCase
import ru.student.detected.shoplist.domain.ShopItem
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val getShopListUseCase: GetShopListUseCase
) : ViewModel() {

    val shopList = getShopListUseCase.getShopList()

    private val scope = viewModelScope
    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        scope.launch {
            editShopItemUseCase.editShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        scope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }
}