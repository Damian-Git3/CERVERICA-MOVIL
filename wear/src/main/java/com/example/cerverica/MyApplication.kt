package com.example.cerverica

import android.app.Application
import android.content.Intent
import android.content.IntentFilter

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val serviceIntent = Intent(this, WearableService::class.java)
        startService(serviceIntent)

        val receiver = WearableBroadcastReceiver()

        val filter1 = IntentFilter("com.example.cerverica.LOGOUT")
        registerReceiver(receiver, filter1)

        val filter2 = IntentFilter("com.example.cerverica.CHECK")
        registerReceiver(receiver, filter2)

        val filter3 = IntentFilter("com.example.cerverica.SINC")
        registerReceiver(receiver, filter3)


    }
}