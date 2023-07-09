package ru.student.detected.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import ru.student.detected.shoplist.R
import ru.student.detected.shoplist.databinding.ActivityShopItemBinding
import ru.student.detected.shoplist.domain.ShopItem.Companion.UNDEFINED_ID

class ShopItemActivity : AppCompatActivity() {
    private lateinit var shopItemBinding: ActivityShopItemBinding
    private lateinit var shopItemViewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopItemBinding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(shopItemBinding.root)
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id)
            return intent
        }
    }
}