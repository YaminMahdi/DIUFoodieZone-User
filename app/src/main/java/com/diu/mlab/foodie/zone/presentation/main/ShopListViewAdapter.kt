package com.diu.mlab.foodie.zone.presentation.main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diu.mlab.foodie.zone.databinding.ItemShopBinding
import com.diu.mlab.foodie.zone.domain.model.ShopProfile
import com.diu.mlab.foodie.zone.util.getDrawable

class ShopListViewAdapter(
    private val shopList: List<ShopProfile>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemShopBinding,
        private val contest: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            list: List<ShopProfile>,
            position: Int
        ) {
            binding.shopNm.text = list[position].shopInfo.nm
            list[position].shopInfo.pic.getDrawable{ binding.logo.setImageDrawable(it) }
            binding.foodRecyclerView.adapter = FoodListViewAdapter(list[position].foodList,list[position].shopInfo.email)
            binding.shopView.setOnClickListener {
                Log.d("TAG", "bindView: startActivity")
                contest.startActivity(Intent(contest, ShopViewActivity::class.java).putExtra("shopIndex",position))
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            ItemShopBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false), viewGroup.context
        )

    override fun getItemCount() = shopList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(shopList[position].shopInfo.visible)
            (holder as ViewHolder).bindView(shopList, position)
    }
}
