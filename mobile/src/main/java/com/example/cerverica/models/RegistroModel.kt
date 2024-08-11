package com.example.cerverica.models

data class RegisterRequest(
    val email: String,
    val fullName: String,
    val password: String
)

data class RegisterResponse(
    val isSuccess: Boolean,
    val message: String
)

data class ErrorResponse(
    val isSuccess: Boolean,
    val message: String
)