package com.ues.boletos.api.responses

data class CarreraResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val lugar: String
)
