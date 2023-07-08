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
        parseIntent()
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        launchCurrentMode()

        addOnTextChangeListeners()

        observeErrorInputs()

        observeCloseScreen()
    }

    private fun launchCurrentMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun observeCloseScreen() {
        shopItemViewModel.closeScreen.observe(this) {
            finish()
        }
    }

    private fun observeErrorInputs() {
        shopItemViewModel.errorInputName.observe(this) {
            val message = if (it) getString(R.string.incorrent_name) else null
            shopItemBinding.etName.error = message
        }

        shopItemViewModel.errorInputCount.observe(this) {
            val message = if (it) getString(R.string.incorrent_count) else null
            shopItemBinding.etCount.error = message
        }
    }

    private fun addOnTextChangeListeners() {
        shopItemBinding.etName.doOnTextChanged { _, _, _, _ ->
            shopItemViewModel.resetErrorInputName()
        }
        shopItemBinding.etCount.doOnTextChanged { _, _, _, _ ->
            shopItemViewModel.resetErrorInputCount()
        }
    }

    private fun launchAddMode() {
        shopItemBinding.saveButton.setOnClickListener {
            shopItemViewModel.addShopItem(
                shopItemBinding.etName.text.toString(),
                shopItemBinding.etCount.text.toString()
            )
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItem.observe(this) {
            (shopItemBinding.etName as TextView).text = it.name
            (shopItemBinding.etCount as TextView).text = it.count.toString()
        }
        shopItemBinding.saveButton.setOnClickListener {
            shopItemViewModel.editShopItem(
                shopItemBinding.etName.text.toString(),
                shopItemBinding.etCount.text.toString()
            )
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Screen mode params is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Screen mode is unknown: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
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
}