package ru.student.detected.shoplist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.*

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val errorInputName: LiveData<Boolean>
        get() = _errorInputName
    private val _errorInputName: MutableLiveData<Boolean> = MutableLiveData()

    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount
    private val _errorInputCount: MutableLiveData<Boolean> = MutableLiveData()

    val shopItem: LiveData<ShopItem>
        get() = _shopItem
    private val _shopItem = MutableLiveData<ShopItem>()

    val closeScreen: LiveData<Unit>
        get() = _closeScreen
    private val _closeScreen = MutableLiveData<Unit>()

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count: Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if (fieldsIsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            closeScreen()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count: Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if (fieldsIsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
            }
            closeScreen()
        }
    }

    private fun closeScreen() {
        _closeScreen.value = Unit
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            parseName(inputCount).toInt()
        } catch (_: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

}