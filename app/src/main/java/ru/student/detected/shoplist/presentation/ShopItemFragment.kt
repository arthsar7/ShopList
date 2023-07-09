package ru.student.detected.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.student.detected.shoplist.R
import ru.student.detected.shoplist.databinding.FragmentShopItemBinding
import ru.student.detected.shoplist.domain.ShopItem.Companion.UNDEFINED_ID

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = UNDEFINED_ID,
) : Fragment() {
    private lateinit var shopItemBinding: FragmentShopItemBinding
    private lateinit var shopItemViewModel: ShopItemViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        shopItemBinding = FragmentShopItemBinding.inflate(inflater, container, false)
        return shopItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
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
        shopItemViewModel.closeScreen.observe(viewLifecycleOwner) {
            finish()
        }
    }

    private fun observeErrorInputs() {
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.incorrent_name) else null
            shopItemBinding.etName.error = message
        }

        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
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
        shopItemViewModel.shopItem.observe(viewLifecycleOwner) {
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

    private fun parseParams() {
        if (screenMode != MODE_ADD && screenMode != MODE_EDIT)
            throw RuntimeException("Screen mode params is absent")
        if (screenMode == MODE_EDIT && shopItemId == UNDEFINED_ID)
            throw RuntimeException("Param shop item id is absent")
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