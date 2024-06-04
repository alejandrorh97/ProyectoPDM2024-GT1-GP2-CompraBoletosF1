package com.ues.boletos.api.responses

data class PerfilResponse(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val telefono: String,
    val fecha_nacimiento: String,
    val genero: String,
)
