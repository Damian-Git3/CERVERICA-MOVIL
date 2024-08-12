package com.example.cerverica

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.LoginRequest
import com.example.cerverica.models.LoginResponse
import com.example.cerverica.controllers.AdminActivity
import com.example.cerverica.controllers.cliente.ClienteActivity
import com.example.cerverica.controllers.EmpleadoActivity
import com.example.cerverica.controllers.RegistroActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<TextView>(R.id.register_button)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)

            RetrofitClient.instance.postLogin(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Log.d("API_RESPONSE", "Response: ${response}")
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.isSuccess == true) {
                            // Guardar el token en SharedPreferences
                            saveToken(loginResponse.token)

                            // Guardar cualquier otra información del usuario según sea necesario
                            saveUserInfo(loginResponse.idUsuario, loginResponse.nombre, loginResponse.role)

                            // Redirigir según el rol del usuario
                            when (loginResponse.role) {  // Suponiendo que "roles" es una lista y tomas el primer rol
                                "Admin" -> startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                                "Produccion" -> startActivity(Intent(this@LoginActivity, EmpleadoActivity::class.java))
                                "Cliente" -> startActivity(Intent(this@LoginActivity, ClienteActivity::class.java))
                                else -> Toast.makeText(this@LoginActivity, "Rol desconocido", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "No successful: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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
}
