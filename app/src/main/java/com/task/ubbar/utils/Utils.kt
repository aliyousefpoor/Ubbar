package com.task.ubbar.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Utils {
    fun isValidIranianPhoneNumber(phone: String): Boolean {
        val iranPhoneRegex = Regex("^09[0-9]{9}$")
        return iranPhoneRegex.matches(phone)
    }

    fun isValidIranLandline(number: String): Boolean {
        val regex = Regex("^0[1-9]{2,3}[0-9]{6,8}$")
        return regex.matches(number)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}