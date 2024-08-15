package com.example.cerverica

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class WearableBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("WEAR_PROCES", "Broadcast recibido!")
        val action = intent?.action
        if (action == "com.example.cerverica.LOGOUT") {
            Log.d("WEAR_PROCES", "paso 3: validar que se trata de una peticion de logout")
            val serviceIntent = Intent(context, WearableService::class.java)
            serviceIntent.putExtra("action", "logout")
            context?.startService(serviceIntent)
        }else if (action == "com.example.cerverica.SINC") {
            val serviceIntent = Intent(context, WearableService::class.java)
            serviceIntent.putExtra("action", "sinc")
            context?.startService(serviceIntent)
        }else if (action == "com.example.cerverica.CHECK") {
            Log.d("WEAR_PROCES", "comprobar el login: broadcast")
            val serviceIntent = Intent(context, WearableService::class.java)
            serviceIntent.putExtra("action", "check")
            context?.startService(serviceIntent)
        }
    }
}