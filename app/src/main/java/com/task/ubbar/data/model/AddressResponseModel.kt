package com.task.ubbar.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressResponseModel(
    val region: Int = 1,
    val address: String,
    val lat: Long,
    val lng: Long,
    val coordiante_mobile: String,
    val coordiante_phone_number: String,
    val first_name: String,
    val last_name: String,
    val gender: String,
    val id: Int? = null
):Parcelable