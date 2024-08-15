package com.example.cerverica

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Guardar el contexto de la aplicación
        context = applicationContext

    }

    companion object {
        lateinit var context: Context
            private set
    }
}
