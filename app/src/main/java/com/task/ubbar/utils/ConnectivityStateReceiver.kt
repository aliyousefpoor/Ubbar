package com.task.ubbar.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.task.ubbar.utils.Utils.isNetworkAvailable

class ConnectivityStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = isNetworkAvailable(context)
        Toast.makeText(
            context,
            if (!isConnected) "No internet connection" else "Internet connection available",
            Toast.LENGTH_SHORT
        ).show()
    }
}