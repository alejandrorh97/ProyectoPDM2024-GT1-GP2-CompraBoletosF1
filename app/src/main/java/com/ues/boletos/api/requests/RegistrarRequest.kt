package com.ues.boletos.api.requests

data class RegistrarRequest(
    val nombre : String,
    val apellido : String,
    val email : String,
    val password : String,
    val fecha_nacimiento : String,
    val telefono : String,
    val genero : String,
)
