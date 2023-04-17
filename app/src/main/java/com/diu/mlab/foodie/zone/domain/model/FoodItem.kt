package com.diu.mlab.foodie.zone.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class FoodItem (
    val key : String="",
    val nm : String="",
    val pic : String="",
    val price : String="",
    val time: String="",
    val status : String=""
): Parcelable

