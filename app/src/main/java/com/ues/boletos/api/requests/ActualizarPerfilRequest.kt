package com.ues.boletos.api.requests

data class ActualizarPerfilRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val fecha_nacimiento: String,
    val genero: String,
)
