package ru.student.detected.shoplist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.student.detected.shoplist.domain.ShopItem
import ru.student.detected.shoplist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {
    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()
    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        addShopItem(shopItem)
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> =
        shopListDao.getShopList().map { mapper.mapListDbModelToListEntity(it) }
//        return MediatorLiveData<List<ShopItem>>().apply {
//            addSource(shopListDao.getShopList()){
//                value = mapper.mapListDbModelToListEntity(it)
//            }
//        }
//    }
}