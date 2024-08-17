package com.example.cerverica.apiservice

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cerverica.MyApplication

object RetrofitClient {

    private const val BASE_DOMAIN = "http://192.168.100.3:5000"
    //private const val BASE_DOMAIN = "http://10.0.2.2:5000"

    private const val BASE_URL = "${BASE_DOMAIN}/api/"

    private fun getToken(): String? {
        val sharedPref = MyApplication.context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        return sharedPref.getString("token", null)
    }
    private val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()

        // Si el token está disponible, agregar el header de autorización
        getToken()?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        Log.d("API_REQUEST", "Token: ${getToken()}")
        // Crear la nueva solicitud con los headers modificados
        val request = requestBuilder.build()
        chain.proceed(request)
    }).build()

    val instance: AuthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(AuthApiService::class.java)
    }
}