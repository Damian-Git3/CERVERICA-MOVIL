package com.example.cerverica

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmpleadoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.activity_empleado)

        GlobalScope.launch {
            semaforo()
        }

    }
    suspend fun semaforo(){
        val rojo = findViewById<TextView>(R.id.rojo)
        val verde = findViewById<TextView>(R.id.verde)
        val amarillo = findViewById<TextView>(R.id.amarillo)

        while (true){
            verde.setBackgroundColor(Color.GREEN)
            delay(5000)
            verde.setBackgroundColor(Color.BLACK)
            delay(500)
            verde.setBackgroundColor(Color.GREEN)
            delay(1000)
            verde.setBackgroundColor(Color.BLACK)
            delay(500)
            verde.setBackgroundColor(Color.GREEN)
            delay(1000)
            verde.setBackgroundColor(Color.BLACK)
            amarillo.setBackgroundColor(Color.YELLOW)
            delay(5000)
            amarillo.setBackgroundColor(Color.BLACK)
            rojo.setBackgroundColor(Color.RED)
            delay(5000)
            rojo.setBackgroundColor(Color.BLACK)
        }
    }
}