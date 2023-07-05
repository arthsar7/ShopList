package ru.student.detected.shoplist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import ru.student.detected.shoplist.R

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel : MainViewModel
    private var test = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.shopList.observe(this) {
            Log.d("TAG", "onCreate: $it")
            if (test == 0) {
                val item = it[0]
                mainViewModel.changeEnableState(item)
                test = 1
            }
        }
    }
}