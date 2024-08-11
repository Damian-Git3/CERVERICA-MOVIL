package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class RecetasResponseModel (
    @SerializedName("id") val idReceta: Int,
    @SerializedName("precioLitro") val precioLitroReceta: Int,
    @SerializedName("nombre") val nombreReceta: String,
    @SerializedName("descripcion") val descripcionReceta: String,
    @SerializedName("imagen") val imagenReceta: String,
    @SerializedName("activo") val activoReceta: Boolean
)

data class RecetaModel (
    @SerializedName("id") val id: Int,
    @SerializedName("precioLitro") val precioLitro: Float,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen") val imagen: String,
    @SerializedName("activo") val activo: Boolean
)