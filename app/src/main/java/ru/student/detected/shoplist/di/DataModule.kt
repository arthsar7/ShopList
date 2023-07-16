package ru.student.detected.shoplist.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.student.detected.shoplist.data.AppDatabase
import ru.student.detected.shoplist.data.ShopListDao
import ru.student.detected.shoplist.data.ShopListRepositoryImpl
import ru.student.detected.shoplist.domain.ShopListRepository

@Module
interface DataModule {

    @Binds
    fun bindRepository(repositoryImpl: ShopListRepositoryImpl): ShopListRepository
    companion object {
        @Provides
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}