package com.example.cerverica

data class NotificacionModel(
    val id: Int,
    val idUsuario: String,
    val fecha: String,
    val tipo: Int,
    val mensaje: String,
    val visto: Boolean
)