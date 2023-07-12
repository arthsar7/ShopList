package ru.student.detected.shoplist.presentation

import android.content.Context
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

class ShopItemFragment : Fragment() {
    private lateinit var shopItemBinding: FragmentShopItemBinding

    private lateinit var shopItemViewModel: ShopItemViewModel

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = UNDEFINED_ID
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else{
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }
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
            onEditingFinishedListener.onEditingFinished()
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
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Screen mode params is absent $screenMode")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Screen mode is unknown: $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent $shopItemId")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(id: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, id)
                }
            }
        }
        interface OnEditingFinishedListener{
            fun onEditingFinished()
        }
    }
}