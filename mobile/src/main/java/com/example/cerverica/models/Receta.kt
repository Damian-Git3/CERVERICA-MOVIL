package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class Receta(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)