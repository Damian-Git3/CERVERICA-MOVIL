package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class Pedido(
    @SerializedName("id") val id: Int,
    @SerializedName("fechaVenta") val fechaVenta: String,
    @SerializedName("totalCervezas") val totalCervezas: Float,
    @SerializedName("metodoEnvio") val metodoEnvio: Int,
    @SerializedName("estatusVenta") val estatusVenta: Int,
    @SerializedName("productosPedido") val productosPedido: List<DetalleVenta>
)