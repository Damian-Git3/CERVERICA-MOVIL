package com.example.cerverica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.LoginRequest
import com.example.cerverica.models.LoginResponse
import com.example.cerverica.controllers.admin.AdminActivity
import com.example.cerverica.controllers.cliente.ClienteActivity
import com.example.cerverica.controllers.empleado.EmpleadoActivity
import com.example.cerverica.controllers.RegistroActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.material.progressindicator.LinearProgressIndicator

class LoginActivity : AppCompatActivity(), DataClient.OnDataChangedListener {

    private lateinit var dataClient: DataClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar DataClient
        dataClient = Wearable.getDataClient(this)

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<TextView>(R.id.register_button)
        val progressBarLogin = findViewById<LinearProgressIndicator>(R.id.progressBarLogin)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        loginButton.setOnClickListener {
            progressBarLogin.visibility = View.VISIBLE

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)

            RetrofitClient.instance.postLogin(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    progressBarLogin.visibility = View.GONE

                    Log.d("API_RESPONSE", "Response: ${response}")
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.isSuccess == true) {
                            // Guardar el token en SharedPreferences
                            saveToken(loginResponse.token)

                            // Guardar cualquier otra información del usuario según sea necesario
                            saveUserInfo(loginResponse.idUsuario, loginResponse.nombre, loginResponse.role)

                            // Enviar datos de login al Wear OS
                            sendLoginDataToWearOS(loginResponse.idUsuario, loginResponse.nombre, loginResponse.role)

                            // Redirigir según el rol del usuario
                            when (loginResponse.role) {  // Suponiendo que "roles" es una lista y tomas el primer rol
                                "Admin" -> startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                                "Operador" -> startActivity(Intent(this@LoginActivity, EmpleadoActivity::class.java))
                                "Cliente" -> startActivity(Intent(this@LoginActivity, ClienteActivity::class.java))
                                else -> Toast.makeText(this@LoginActivity, "Rol desconocido", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Logueo no exitoso! : ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    progressBarLogin.visibility = View.GONE

                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    // Función para guardar el token en SharedPreferences
    private fun saveToken(token: String) {
        val sharedPref = MyApplication.context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("token", token)
        editor.apply()
    }

    // Función para guardar información adicional del usuario en SharedPreferences
    private fun saveUserInfo(idUsuario: String?, nombre: String?, role: String) {
        val sharedPref = MyApplication.context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("idUsuario", idUsuario)
        editor.putString("nombre", nombre)
        editor.putString("role", role)
        editor.apply()
    }

    private fun sendLoginDataToWearOS(idUsuario: String, nombre: String, role: String) {
        val putDataMapRequest = PutDataMapRequest.create("/login_data")
        val dataMap = putDataMapRequest.dataMap
        dataMap.putString("idUsuario", idUsuario)
        dataMap.putString("nombre", nombre)
        dataMap.putString("role", role)
        dataMap.putString("action", "change_activity")  // Añadido para cambiar la actividad
        val request = putDataMapRequest.asPutDataRequest()
        val dataItemTask: Task<DataItem> = dataClient.putDataItem(request)

        dataItemTask.addOnSuccessListener {
            Log.d("WEAR_PROCES", "Login data sent successfully to Wear OS.")
        }

        dataItemTask.addOnFailureListener {
            Log.e("WEAR_PROCES", "Failed to send login data to Wear OS.")
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        // Manejar los datos recibidos si es necesario
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(this)
    }
}
