package com.example.cerverica.controllers

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cerverica.LoginActivity
import com.example.cerverica.MainActivity
import com.example.cerverica.R
import com.example.cerverica.models.ErrorResponse
import com.example.cerverica.apiservice.RetrofitClient
import com.example.cerverica.models.RegisterRequest
import com.example.cerverica.models.RegisterResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val emailEditText = findViewById<EditText>(R.id.email)
        val fullNameEditText = findViewById<EditText>(R.id.full_name)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginButton = findViewById<TextView>(R.id.login_button)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val fullName = fullNameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword){
                register(email=email, fullName=fullName, password=password)
            }else {
                Toast.makeText(this@RegistroActivity, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            redirectToLogin()
        }
    }

    private fun register(email: String, fullName: String, password: String){
        val registerRequest = RegisterRequest(email, fullName, password)

        RetrofitClient.instance.postRegister(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.isSuccess == true) {
                        Toast.makeText(this@RegistroActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        redirectToLogin()
                    } else {
                        Toast.makeText(this@RegistroActivity, registerResponse?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    Toast.makeText(this@RegistroActivity, errorResponse.message, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegistroActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun redirectToLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
