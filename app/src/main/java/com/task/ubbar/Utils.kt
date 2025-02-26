package com.task.ubbar

object Utils {
    fun isValidIranianPhoneNumber(phone: String): Boolean {
        val iranPhoneRegex = Regex("^09[0-9]{9}$")
        return iranPhoneRegex.matches(phone)
    }

    fun isValidIranLandline(number: String): Boolean {
        val regex = Regex("^0[1-9]{2,3}[0-9]{6,8}$")
        return regex.matches(number)
    }
}