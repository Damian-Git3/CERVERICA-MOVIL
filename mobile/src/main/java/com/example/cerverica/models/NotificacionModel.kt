package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class NotificacionModel(
    @SerializedName("id") val id: Int,
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tipo") val tipo: Int,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("visto") val visto: Boolean
)
