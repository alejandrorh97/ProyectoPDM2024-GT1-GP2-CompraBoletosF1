package com.ues.boletos.models

data class User(
    val id: Int,
    val rolId: Int,
    val email: String,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val fechaNacimiento: String,
    val sexo: String,
    val token: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String
)

data class NewUser(
    val email: String,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val fechaNacimiento: String,
    val sexo: String, // Hombre, Mujer
    val password: String
)
