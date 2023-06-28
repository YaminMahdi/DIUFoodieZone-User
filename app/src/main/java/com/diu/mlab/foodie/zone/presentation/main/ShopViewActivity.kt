package com.diu.mlab.foodie.zone.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.diu.mlab.foodie.zone.R
import com.diu.mlab.foodie.zone.databinding.ActivityShopViewBinding
import com.diu.mlab.foodie.zone.util.getDrawable
import com.diu.mlab.foodie.zone.util.setBounceClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShopViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopViewBinding
    private val viewModel by viewModels<UserMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopViewBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_FoodieZone_windowTranslucentStatus);
        setContentView(binding.root)

//        window.statusBarColor = getColor(R.color.trans)

        val shopIndex = intent.getIntExtra("shopIndex", 0)
        viewModel.getShopProfileList {
            MainScope().launch {
                Toast.makeText(this@ShopViewActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.shopProfileList.observe(this){lst ->
            if(lst.size > shopIndex) {
                val shopInfo = lst[shopIndex].shopInfo
                shopInfo.pic.getDrawable { binding.pic.setImageDrawable(it) }
                shopInfo.cover.getDrawable { binding.cover.setImageDrawable(it) }
                binding.shopNm.text = shopInfo.nm

                binding.foodRecyclerView.adapter = FoodListViewAdapter(lst[shopIndex].foodList, shopInfo.email, 1)
            }
        }
        binding.btnBack.setBounceClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}