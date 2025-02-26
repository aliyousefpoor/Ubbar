package com.task.ubbar.utils

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log

class ConnectivityMonitorService : Service() {

    private lateinit var connectivityReceiver: ConnectivityStateReceiver

    override fun onCreate() {
        super.onCreate()

        connectivityReceiver = ConnectivityStateReceiver()
        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivityReceiver, filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        Log.d("ConnectivityMonitor", "Service stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

}
