package com.example.cerverica.models.cliente

import com.google.gson.annotations.SerializedName
import java.util.Date

data class RecetaPackModel (
    @SerializedName("id") val idReceta: Int,
    @SerializedName("nombre") val nombreReceta: String,
    @SerializedName("especificaciones") val especificacionesReceta: String,
    @SerializedName("precioPaquete1") val precioPaquete1Receta: Float,
    @SerializedName("precioPaquete6") val precioPaquete6Receta: Float,
    @SerializedName("precioPaquete12") val precioPaquete12Receta: Float,
    @SerializedName("precioPaquete24") val precioPaquete24Receta: Float,
    @SerializedName("fechaRegistrado") val fechaRegistroReceta: String,
    @SerializedName("imagen") val imagenReceta: String,
    @SerializedName("rutaFondo") val imagenFondoReceta: String
)

data class RecetaModel (
    @SerializedName("id") val id: Int,
    @SerializedName("precioLitro") val precioLitro: Float,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen") val imagen: String,
    @SerializedName("activo") val activo: Boolean
)

data class RecetaFavoritoModel (
    @SerializedName("id") val idFavorito: Int,
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("idReceta") val idReceta: Int,
    @SerializedName("nombre") val nombreReceta: String,
    @SerializedName("descripcion") val descripcionReceta: String,
    @SerializedName("especificaciones") val especificacionesReceta: String,
    @SerializedName("imagen") val imagenReceta: String,
    @SerializedName("rutaFondo") val imagenFondoReceta: String
)

data class RecetaEliminarFavoritoRequest(
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("idReceta") val idReceta: Int
)

data class RecetaAgregarFavoritoRequest(
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("idReceta") val idReceta: Int
)