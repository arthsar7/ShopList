package ru.student.detected.shoplist.presentation

import android.app.Application
import ru.student.detected.shoplist.di.DaggerApplicationComponent

class ShopListApp: Application() {
    val component by lazy{
        DaggerApplicationComponent.factory().create(this)
    }
}