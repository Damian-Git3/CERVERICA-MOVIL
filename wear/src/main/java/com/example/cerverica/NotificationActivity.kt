package com.example.cerverica

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationActivity : ComponentActivity() {
    private lateinit var adapter: NotificacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        val inicio = findViewById<Button>(R.id.inicio)

        inicio.setOnClickListener {
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_notificaciones)

        // Obtener el JSON string de las notificaciones
        val jsonString = intent.getStringExtra("notificaciones")

        if (!jsonString.isNullOrEmpty()) {
            // Deserializar el JSON string a una lista de NotificacionModel
            val gson = Gson()
            val tipoLista = object : TypeToken<List<NotificacionModel>>() {}.type
            val listaNotificaciones: List<NotificacionModel> = gson.fromJson(jsonString, tipoLista)

            // Configurar el RecyclerView con el adaptador
            adapter = NotificacionAdapter(listaNotificaciones) { notificacion ->
                // Acción a realizar cuando se hace clic en una notificación
                onNotificacionClick(notificacion)
            }

            val botonOrden = findViewById<TextView>(R.id.btnAscendente)

            var ascendente = true
            botonOrden.setOnClickListener {
                if (ascendente){
                    adapter.ordenarAscendente(true)
                    botonOrden.text = "Fecha Ascendente"
                } else{
                    adapter.ordenarAscendente(false)
                    botonOrden.text = "Fecha Descendente"
                }
                ascendente = !ascendente
            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun onNotificacionClick(notificacion: NotificacionModel) {
        val ask = Intent("com.example.cerverica.FIRE_NOTIFICATION")
        ask.putExtra("id",notificacion.id)
        sendBroadcast(ask)
    }
}

