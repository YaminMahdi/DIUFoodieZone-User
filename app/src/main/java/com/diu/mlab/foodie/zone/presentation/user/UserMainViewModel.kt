package com.diu.mlab.foodie.zone.presentation.user

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diu.mlab.foodie.zone.domain.model.FoodItem
import com.diu.mlab.foodie.zone.domain.model.ShopProfile
import com.diu.mlab.foodie.zone.domain.use_cases.user.UserMainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMainViewModel @Inject constructor(
    private val mainUseCases: UserMainUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val shopProfileList= savedStateHandle.getLiveData<List<ShopProfile>>("shopProfileList")
    val currentFood= savedStateHandle.getLiveData<FoodItem>("food")


    fun getShopProfileList(
        failed :(msg : String) -> Unit
    ){
        viewModelScope.launch {
            mainUseCases.getShopProfileList({
//                val tmpList = it.toList()
                savedStateHandle["shopProfileList"]= it
                Log.d("TAG", "getShopProfileList: list loaded $it")
            },failed)
        }
    }

    fun getFoodDetails(
        shopEmail: String,
        foodId: String,
        failed: (msg: String) -> Unit
    ){
        viewModelScope.launch {
            mainUseCases.getFoodDetails(shopEmail,foodId,{
                savedStateHandle["food"]= it
            },failed)
        }
    }

}