package ru.student.detected.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.student.detected.shoplist.R
import ru.student.detected.shoplist.databinding.ActivityShopItemBinding
import ru.student.detected.shoplist.domain.ShopItem.Companion.UNDEFINED_ID
import javax.inject.Inject

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.Companion.OnEditingFinishedListener {
    private lateinit var shopItemBinding: ActivityShopItemBinding

    @Inject
    lateinit var shopViewModelFactory: ShopViewModelFactory
    private lateinit var shopItemViewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID
    private val component by lazy{
        (application as ShopListApp).component
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        shopItemBinding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(shopItemBinding.root)
        parseIntent()
        shopItemViewModel =
            ViewModelProvider(this, shopViewModelFactory)[ShopItemViewModel::class.java]
        if (savedInstanceState == null) {
            launchCurrentMode()
        }
    }

    private fun launchCurrentMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> throw RuntimeException("Screen mode is unknown: $screenMode")
        }
        supportFragmentManager.beginTransaction().replace(R.id.shop_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Screen mode params is absent $screenMode")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Screen mode is unknown: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent $shopItemId")
            } else {
                shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, UNDEFINED_ID)
            }
        }
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

    override fun onEditingFinished() {
        finish()
    }
}