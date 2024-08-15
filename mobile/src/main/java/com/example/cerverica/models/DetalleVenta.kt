package com.example.cerverica.models

import com.google.gson.annotations.SerializedName

data class DetalleVenta(
    @SerializedName("id") val id: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("pack") val pack: Int,
    @SerializedName("idStock") val idStock: Int,
    @SerializedName("stock") val stock: Stock?
)