package ru.student.detected.shoplist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.student.detected.shoplist.databinding.FragmentShopItemBinding

class ShopItemFragment : Fragment() {
    private lateinit var shopItemBinding: FragmentShopItemBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        shopItemBinding = FragmentShopItemBinding.inflate(inflater, container, false)
        return shopItemBinding.root
    }
}