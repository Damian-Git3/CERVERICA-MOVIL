package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("id") val id: Int,
    @SerializedName("idReceta") val idReceta: Int,
    @SerializedName("receta") val receta: Receta?
)