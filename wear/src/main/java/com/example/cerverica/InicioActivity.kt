package com.example.cerverica

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InicioActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio)

        val logoutButton = findViewById<Button>(R.id.logout)

        logoutButton.setOnClickListener {
            Log.d("WEAR_PROCES", "paso 1: generar click")
            val logoutIntent = Intent("com.example.cerverica.LOGOUT")
            sendBroadcast(logoutIntent)
        }

        val cuentaButton = findViewById<TextView>(R.id.cuenta)
        cuentaButton.setOnClickListener {
            val verCuenta = Intent("com.example.cerverica.INFO")
            sendBroadcast(verCuenta)
        }

        val notificiaciones = findViewById<Button>(R.id.notificaciones)

        notificiaciones.setOnClickListener {
            val ask = Intent("com.example.cerverica.NOTIFICATIONS")
            sendBroadcast(ask)
        }
    }
}
