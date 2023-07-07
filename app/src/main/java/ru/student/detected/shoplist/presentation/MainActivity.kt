package ru.student.detected.shoplist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.student.detected.shoplist.R
import ru.student.detected.shoplist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var shopListAdapter: ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setupRecyclerView()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        shopListAdapter = ShopListAdapter()
        with(mainBinding.rvShopList) {
            adapter = shopListAdapter

            mainBinding.rvShopList.recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_disabled,
                ShopListAdapter.MAX_VH_POOL_SIZE
            )

            mainBinding.rvShopList.recycledViewPool.setMaxRecycledViews(
                R.layout.item_shop_enabled,
                ShopListAdapter.MAX_VH_POOL_SIZE
            )
        }

        setupOnShopItemLongClickListener()

        setupOnShopItemClickListener()

        setupOnShopItemSwipeListener()

    }

    private fun setupOnShopItemSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val curItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                mainViewModel.deleteShopItem(curItem)
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(mainBinding.rvShopList)
    }

    private fun setupOnShopItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("ADAPTER", "onShopItemClickListener: $it")
        }
    }

    private fun setupOnShopItemLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            mainViewModel.changeEnableState(it)
        }
    }
}