package com.example.cerverica

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class WearableBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("WEAR_PROCES", "paso 2: ingresar al broadcast")
        val action = intent?.action
        if (action == "com.example.cerverica.LOGIN_ACTION") {
            Log.d("WEAR_PROCES", "paso 3: enviar la solicitud de login al servicio ")
            val idUsuario = intent.getStringExtra("idUsuario")
            val nombre = intent.getStringExtra("nombre")
            val role = intent.getStringExtra("role")
            val serviceIntent = Intent(context, MobileWearableService::class.java)
            serviceIntent.putExtra("action", "login")
            serviceIntent.putExtra("idUsuario", idUsuario)
            serviceIntent.putExtra("nombre", nombre)
            serviceIntent.putExtra("role", role)
            context?.startService(serviceIntent)
        } else if (action == "com.example.cerverica.LOGOUT_ACTION") {
            val serviceIntent = Intent(context, MobileWearableService::class.java)
            serviceIntent.putExtra("action", "logout")
            context?.startService(serviceIntent)
        }
    }
}
