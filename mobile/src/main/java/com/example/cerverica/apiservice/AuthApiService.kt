package com.example.cerverica.apiservice


import com.example.cerverica.models.LoginRequest
import com.example.cerverica.models.LoginResponse
import com.example.cerverica.models.RecetaModel
import com.example.cerverica.models.RegisterRequest
import com.example.cerverica.models.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("Account/login")
    fun postLogin(@Body params: LoginRequest): Call<LoginResponse>

    @POST("Account/register")
    fun postRegister(@Body params: RegisterRequest): Call<RegisterResponse>

    @POST("Account/logout")
    fun postLogout(@Header("Authorization") token: String): Call<ResponseBody>

    @GET("Receta")
    fun getRecetas():Call<List<RecetaModel>>
}