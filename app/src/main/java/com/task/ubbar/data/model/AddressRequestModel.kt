package com.task.ubbar.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressRequestModel(
    val region: Int = 1,
    val address: String,
    var lat: Long,
    var lng: Long,
    val coordiante_mobile: String,
    val coordiante_phone_number: String,
    val first_name: String,
    val last_name: String,
    val gender: String
) : Parcelable