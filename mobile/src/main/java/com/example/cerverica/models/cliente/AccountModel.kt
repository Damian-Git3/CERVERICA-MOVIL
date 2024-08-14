package com.example.cerverica.models.cliente

import com.google.gson.annotations.SerializedName

data class AccountModel (
    @SerializedName("id") val idUsuario: String,
    @SerializedName("fullName") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("roles") val roles: List<String>,
    @SerializedName("activo") val activo: Boolean
)