package ru.student.detected.shoplist.presentation

import androidx.lifecycle.ViewModel
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.AddShopItemUseCase
import ru.student.detected.shoplist.domain.EditShopItemUseCase
import ru.student.detected.shoplist.domain.GetShopItemUseCase
import ru.student.detected.shoplist.domain.ShopItem

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count : Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if(fieldsIsValid){
            val shopItem = ShopItem(name, count, true)
            val item = addShopItemUseCase.addShopItem(shopItem)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count : Int = parseCount(inputCount)
        val fieldsIsValid = validateInput(name, count)
        if(fieldsIsValid){
            val shopItem = ShopItem(name, count, true)
            val item = editShopItemUseCase.editShopItem(shopItem)
        }
    }

    private fun parseName(inputName: String?) : String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?) : Int{
        return try {
            parseName(inputCount).toInt()
        } catch (_: Exception){
            0
        }
    }

    private fun validateInput(name: String, count: Int) : Boolean{
        var result = true
        if(name.isBlank()){
            // TODO("Show input name error")
            result = false
        }
        if(count <= 0){
            // TODO("Show input count error")

            result = false
        }
        return result
    }

}