package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("rol") val role: String
)