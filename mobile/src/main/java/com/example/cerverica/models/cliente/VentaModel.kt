package com.example.cerverica.models.cliente

import com.google.gson.annotations.SerializedName

data class VentaModel (
    @SerializedName("id") val id: Int,
    @SerializedName("fechaVenta") val fechaVenta: String,
    @SerializedName("total") val total: Float,
    @SerializedName("metodoPago") val metodoPago: Int,
    @SerializedName("metodoEnvio") val metodoEnvio: Int,
    @SerializedName("estatusVenta") val estatusVenta: Int
)