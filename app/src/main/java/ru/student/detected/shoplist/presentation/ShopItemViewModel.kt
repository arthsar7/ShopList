package ru.student.detected.shoplist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.*

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val scope = viewModelScope
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
        scope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.value = item
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count: Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if (fieldsIsValid) {
            scope.launch {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                closeScreen()
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count: Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if (fieldsIsValid) {
            _shopItem.value?.let {
                scope.launch {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    closeScreen()
                }
            }
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