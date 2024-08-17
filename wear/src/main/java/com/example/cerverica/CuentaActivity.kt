package com.example.cerverica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class CuentaActivity : ComponentActivity() {
    private val TAG = "WEAR_PROCES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        // Referencia a los TextView
        val txtNombre = findViewById<TextView>(R.id.nombre)
        val txtEmail = findViewById<TextView>(R.id.email)
        val txtCuenta = findViewById<TextView>(R.id.rol)

        // Obtener el Bundle del Intent
        val bundle = intent.extras

        if (bundle != null) {
            // Extraer los datos del Bundle
            val nombre = bundle.getString("nombre", "Nombre no disponible")
            val email = bundle.getString("email", "Email no disponible")
            val role = bundle.getString("role", "Rol no disponible")

            // Imprimir en el log para verificar
            Log.d(TAG, "Nombre: $nombre")
            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Role: $role")

            // Asignar los textos a los TextView
            txtNombre.text = nombre
            txtEmail.text = email
            txtCuenta.text = role
        } else {
            Log.d(TAG, "Bundle es nulo")
        }

        // Configurar el botón para regresar a la actividad de inicio
        val regresarBoton = findViewById<Button>(R.id.inicio)
        regresarBoton.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }
    }
}
