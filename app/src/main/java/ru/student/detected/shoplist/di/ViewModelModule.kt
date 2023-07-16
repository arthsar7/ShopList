package ru.student.detected.shoplist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.student.detected.shoplist.presentation.MainViewModel
import ru.student.detected.shoplist.presentation.ShopItemViewModel

@Module
interface ViewModelModule {
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(mainViewModel: ShopItemViewModel): ViewModel

}